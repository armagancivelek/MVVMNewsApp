package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.NewActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class ArticleFragment: Fragment(R.layout.fragment_article) {
    lateinit var  viewModel:NewsViewModel
     val args:ArticleFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this,object:
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                val graph=findNavController().navInflater.inflate(R.navigation.news_nav_graph)

                graph.startDestination=R.id.breakingNewsFragment
                findNavController().graph=graph

            }
        }
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewActivity).viewModel

        val article=args.article
        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }




        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Haber başarıyla eklendi",Snackbar.LENGTH_SHORT).show()
        }


     }




}
