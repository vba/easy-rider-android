package com.renault.android.instantapps.map

import android.app.FragmentManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.renault.android.instantapps.map.MapViewFragment.Companion.SERVICE_DUE_THRESHOLD
import com.renault.android.instantapps.map.tools.getRandomCoordinates
import jp.renault.android.instantapps.base.event.FuelLevelEvent
import org.funktionale.option.getOrElse
import org.funktionale.option.toOption
import jp.renault.android.instantapps.base.event.ServiceDueEvent
import org.funktionale.option.Option
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe




class MapViewFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private val TAG = MapViewFragment::javaClass.name
        private val SERVICE_DUE_THRESHOLD = 5
        private val FUEL_LEVEL_THRESHOLD = 10
    }

    private var map: GoogleMap? = null
    private var gasStationMarkers = listOf<Marker>()
    private var serviceMarkers = listOf<Marker>()

    private val myLocation = LatLng(48.85661400000001, 2.3522219000000177)
    private val destination = LatLng(48.86208387947027, 2.3449205607175827)

    private val toIcon = BitmapDescriptorFactory::fromResource


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onServiceDueEvent(event: ServiceDueEvent) {
        if (event.duePercentage > SERVICE_DUE_THRESHOLD) {
            clearServiceMarkers()
            return
        }
        regenerateNearestServicesMarkers()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFuelLevelEvent(event: FuelLevelEvent) {
        if(event.remainsPercentage > FUEL_LEVEL_THRESHOLD) {
            clearGasStationMarkers()
            return
        }
        generateNearestGasMarkers()
    }

    private fun generateNearestMarkers(map: GoogleMap, resource:Int, max: Int = 10): List<Marker> {
        return (1 .. max).map {
            MarkerOptions()
                .title(("Point $it"))
                .position(getRandomCoordinates(myLocation))
                .icon(toIcon(resource))
        }.fold(listOf<Marker>(), { acc, x -> acc.plus(map.addMarker(x)) })
    }

    private fun regenerateNearestServicesMarkers() {
        clearServiceMarkers()
        getGoogleMap()
            .map { generateNearestMarkers(it, R.drawable.repair_service) }
            .map { serviceMarkers = it }
    }

    private fun clearServiceMarkers() = serviceMarkers.map { it.remove() }

    private fun clearGasStationMarkers() = gasStationMarkers.map { it.remove() }

    private fun generateNearestGasMarkers() {
        clearGasStationMarkers()
        getGoogleMap()
            .map { generateNearestMarkers(it, R.drawable.gasoline_pump) }
            .map { gasStationMarkers = it }
    }

    override fun onMapReady(map: GoogleMap?) {
        val build = {x:LatLng, y:Int -> MarkerOptions().title("point").position(x).icon(toIcon(y))}
        this.map = map.toOption()
            .map { it.addMarker(build(myLocation, R.drawable.car_placeholder)); it }
            .map { it.addMarker(build(destination, R.drawable.parking)); it }
            .map { it.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14.1f)); it }
            .getOrElse { null }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false);

        getMapFragment()
            .map { it.getMapAsync(this) }
            .map { true }
            .getOrElse { false }

        return view
    }

    private fun getMapFragment(): Option<MapFragment> = getLocalFragmentManager()
        .map { it.findFragmentById(R.id.map_fragment) as MapFragment }

    private fun getGoogleMap(): Option<GoogleMap> = map.toOption()

    private fun getLocalFragmentManager(): Option<FragmentManager> =
        activity.toOption().flatMap { it.fragmentManager.toOption() }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}