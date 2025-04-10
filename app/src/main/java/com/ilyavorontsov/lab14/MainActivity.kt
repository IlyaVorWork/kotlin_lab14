package com.ilyavorontsov.lab14

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import java.io.Serializable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class City(
    val title: String,
    val region: String,
    val district: String,
    val postalCode: String,
    val timezone: String,
    val population: String,
    val founded: String,
    val lat: Float,
    val lon: Float
) : Serializable

class CityViewModel : ViewModel() {
    private val selectedCity = MutableLiveData<City>()
    val selectedItem: LiveData<City> get() = selectedCity

    fun selectCity(item: City) {
        selectedCity.value = item
    }
}

class MainActivity : AppCompatActivity() {

    private val cityVM: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null && this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view, CitiesList())
                .commit()
        }
        supportFragmentManager.setFragmentResultListener("chosen_city", this) { key, bundle ->
            val result = bundle.getSerializable("city") as City
            cityVM.selectCity(result)
            if (this.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE)
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, ChosenCity()).setReorderingAllowed(true).addToBackStack(null).commit()
        }
    }
}