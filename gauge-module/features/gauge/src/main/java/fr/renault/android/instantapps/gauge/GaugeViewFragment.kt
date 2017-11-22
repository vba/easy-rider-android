package fr.renault.android.instantapps.gauge


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import jp.renault.android.instantapps.base.event.raw.FuelLevelEvent
import jp.renault.android.instantapps.base.event.raw.ServiceDueInDistanceEvent
import org.funktionale.currying.curried
import org.funktionale.option.Option
import org.funktionale.option.toOption
import org.greenrobot.eventbus.EventBus

typealias ArrAdapter = ArrayAdapter<CharSequence>

class GaugeViewFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private val metersPerKm = 1_000.00

    private val bindSpinner = { x: View, y:ArrAdapter ->
        x.findViewById<Spinner>(R.id.distance_from_the_car_spinner).toOption().map { it.adapter = y; y }
    }.curried()

    private val mapContextToArrayAdapter = { x: Context ->
        ArrayAdapter.createFromResource(x, R.array.distance_from_car_array, android.R.layout.simple_spinner_item)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id) {
            R.id.seekBar_fuel_level -> onFuelLevelProgress(seekBar, progress)
            R.id.seekBar_service_due_in -> onServiceDueInProgress(seekBar, progress)
            else -> Unit
        }
    }

    private fun onFuelLevelProgress(seekBar: SeekBar, progress: Int) {
        getDistanceFromSpinner()
            .map { EventBus.getDefault().post(FuelLevelEvent(progress, it, GaugeViewFragment::class.java)) }
    }

    private fun getDistanceFromSpinner(): Option<Double> {
        return view.toOption()
            .flatMap { it.findViewById<Spinner>(R.id.distance_from_the_car_spinner).toOption() }
            .flatMap { (it.selectedItem as String).toOption() }
            .flatMap { it.replace("m", "").toDoubleOrNull().toOption() }
            .map { it / metersPerKm }
    }

    private fun onServiceDueInProgress(seekBar: SeekBar, progress: Int) {
        getDistanceFromSpinner().map {
            EventBus.getDefault().post(ServiceDueInDistanceEvent(progress, it, GaugeViewFragment::class.java))
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_gauge_view, container, false)

        listOf(R.id.seekBar_fuel_level, R.id.seekBar_service_due_in)
            .map { view.findViewById<SeekBar>(it)}
            .fold(this, {acc, x -> x.setOnSeekBarChangeListener(acc); acc })

        context.toOption().map(mapContextToArrayAdapter)
            .flatMap(bindSpinner(view))
            .map { x -> x.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        return view
    }

}
