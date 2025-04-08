package com.ilyavorontsov.lab14

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChosenCity.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChosenCity : Fragment() {

    private val cityVM: CityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chosen_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonOpenMap: Button = view.findViewById(R.id.openMapButton)

        val tvCity: TextView = view.findViewById(R.id.tvCityLabel)
        val tvFederalDistrict: TextView = view.findViewById(R.id.tvFederalDistrictLabel)
        val tvRegion: TextView = view.findViewById(R.id.tvRegionLabel)
        val tvPostCode: TextView = view.findViewById(R.id.tvPostCodeLabel)
        val tvTimeZone: TextView = view.findViewById(R.id.tvTimeZoneLabel)
        val tvPopulation: TextView = view.findViewById(R.id.tvPopulationLabel)
        val tvFounded: TextView = view.findViewById(R.id.tvFoundedLabel)

        cityVM.selectedItem.observe(this, Observer { city ->
            tvCity.text = tvCity.text.split(":")[0] + ": " + city.title
            tvFederalDistrict.text = tvFederalDistrict.text.split(":")[0] + ": " + city.district
            tvRegion.text = tvRegion.text.split(":")[0] + ": " + city.region
            tvPostCode.text = tvPostCode.text.split(":")[0] + ": " + city.postalCode
            tvTimeZone.text = tvTimeZone.text.split(":")[0] + ": " + city.timezone
            tvPopulation.text = tvPopulation.text.split(":")[0] + ": " + city.population
            tvFounded.text = tvFounded.text.split(":")[0] + ": " + city.founded + " году"
        })

        buttonOpenMap.setOnClickListener {
            val selectedCity = cityVM.selectedItem

            if (selectedCity.value !== null) {
                val intent = Uri.parse("geo:${selectedCity.value!!.lat},${selectedCity.value!!.lon}").let { location ->
                    Intent(Intent.ACTION_VIEW, location)
                }
                startActivity(intent)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container_view, CitiesList()).commit()
        }
    }
}