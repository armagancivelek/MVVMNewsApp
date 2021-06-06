package com.androiddevs.mvvmnewsapp.ui.fragments


import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.R
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*


class MapFragment : Fragment(R.layout.fragment_map),OnMapReadyCallback {
    private var hMap: HuaweiMap? = null
    private var mMapView: MapView? = null
    lateinit var authParams: AccountAuthParams
    lateinit var service: AccountAuthService

    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }
    var mapViewBundle: Bundle? = null




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState !=null){
            mapViewBundle =
                savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)


        }
        init(view)







    }

    private fun init(view:View) {
        authParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setIdToken()
            .createParams()
        service = AccountAuthManager.getService(requireActivity(), authParams)



        mMapView=view.findViewById(R.id.mapView)
        MapsInitializer.setApiKey("CgB6e3x9ON+85Dq8OQKglaLOb/iAF0QPSNTfbZnFxKc8aUcNEOaax2yVV8bzWaAPMEMLxAUne0CYU+F+1bMK/owv")
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this@MapFragment)



    }

    override fun onMapReady(map: HuaweiMap?) {
        hMap=map




     hMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
         LatLng(41.0513,28.979),4.2f,4.2f,20.5f)
     ))
        hMap!!.setOnInfoWindowClickListener { marker -> Toast.makeText(context, "onInfoWindowClick:${marker.title}", Toast.LENGTH_SHORT).show() }

        hMap!!.setOnMapClickListener {latLng ->
           val adres:List<Address>  = Geocoder(context, Locale.getDefault()).getFromLocation(latLng.latitude,
            latLng.longitude,1)

            if(adres.size>0)
            {

                val bundle= Bundle().apply {
                    putSerializable("countryCode", adres[0].countryCode)
                }

                     findNavController().navigate(R.id.action_mapFragment_to_breakingNewsFragment,
                         bundle)

                  }





        }


    }

    override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
    }



    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }


}