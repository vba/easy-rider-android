package fr.renault.android.instantapps.gauge


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import jp.renault.android.instantapps.base.event.FuelLevelEvent
import jp.renault.android.instantapps.base.event.ServiceDueEvent
import org.greenrobot.eventbus.EventBus

class GaugeViewFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
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
        EventBus.getDefault().post(FuelLevelEvent(progress))
    }

    private fun onServiceDueInProgress(seekBar: SeekBar, progress: Int) {
        EventBus.getDefault().post(ServiceDueEvent(progress))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gauge_view, container, false)

        listOf(R.id.seekBar_fuel_level, R.id.seekBar_service_due_in)
            .map { view.findViewById<SeekBar>(it)}
            .fold(this, {acc, x -> x.setOnSeekBarChangeListener(acc); acc })

        return view
    }

}
