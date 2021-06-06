package com.androiddevs.mvvmnewsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.databinding.FragmentLoginScreenBinding
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.SIGN_IN_RESULT_NUMBER
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectUser
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService



class LoginScreenFragment : Fragment(R.layout.fragment_login_screen) {
    lateinit var authParams: AccountAuthParams
    lateinit var service: AccountAuthService
    private var _binding: FragmentLoginScreenBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("ab","oncreateFromFragment")


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentLoginScreenBinding.inflate(inflater,container,false)
        Log.i("ab","oncreateViewFromFragment")
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ab","onViewCreatedFromFragment")
        init()

        eventHandler(view)

    }

    fun eventHandler(view: View) {

       binding.huaweiIdAuthButton
            .setOnClickListener {
               startActivityForResult(service.signInIntent, SIGN_IN_RESULT_NUMBER)
            }
        binding.logInAnonymously.setOnClickListener {


                service.silentSignIn().addOnSuccessListener{authAccount->


                   findNavController().navigate(R.id.action_loginScreen_to_mapFragment)

                }.addOnFailureListener{ e->
                    if (e is ApiException) {
                         Toast.makeText(requireContext(),"Yetkiniz iptal edilmiş " +
                                 "Huawei id ile giriş yapınız",Toast.LENGTH_SHORT).show()
                    }

                }
         }



    }

    private fun init() {
        authParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .createParams()
        service = AccountAuthManager.getService(requireActivity(), authParams)





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_RESULT_NUMBER) {
            val accountAuthTask = AccountAuthManager.parseAuthResultFromIntent(data)

            if (accountAuthTask.isSuccessful) {
                val authAccount = accountAuthTask.result




                findNavController().navigate(R.id.action_loginScreen_to_mapFragment)

            } else
                Toast.makeText(requireActivity(), "Giriş Başarısız", Toast.LENGTH_SHORT).show()


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }



}