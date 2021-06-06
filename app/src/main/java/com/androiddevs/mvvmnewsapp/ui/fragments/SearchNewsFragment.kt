package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.NewActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment: Fragment(R.layout.fragment_search_news) {

    lateinit var  viewModel:NewsViewModel
    private val TAG="ABC"
    lateinit var  newsAdapter:NewsAdapter

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
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {

            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )

        }

        var job: Job?=null

        etSearch.addTextChangedListener {editable->
            job?.cancel()

            job= MainScope().launch {
               delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                     if(editable.toString().isNotEmpty())
                     {
                         viewModel.searchNews(editable.toString())
                     }
                }

            }

        }




        viewModel.searchNews.observe(viewLifecycleOwner, Observer{response->

            Log.d(TAG,"response: ${response.message}")
            when(response){
                is Resource.Success->
                {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }

                is Resource.Error->
                {
                    hideProgressBar()
                    response.message?.let {


                    }
                }

                is Resource.Loading->
                {
                    showProgressBar()
                }
            }


        })

    }
    private fun hideProgressBar()
    {
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar()
    {
        paginationProgressBar.visibility=View.VISIBLE
    }
    private fun setupRecyclerView()
    {
        newsAdapter= NewsAdapter()
        rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)

        }
    }
}