package com.ilyavorontsov.lab14

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager

object Common {
    val cities = mutableListOf<City>()
    fun initCities(ctx: Context) {
        if (cities.isEmpty()) {
            val lines = ctx.resources.openRawResource(R.raw.cities)
                .bufferedReader().lines().toArray()
            for (i in 1 until lines.count()) {
                val parts = lines[i].toString().split(";")
                val city = City(
                    parts[3], // city
                    parts[2], // region
                    parts[1], // federal_district
                    parts[0], // postal_code
                    parts[4], // timezone
                    parts[7], // population
                    parts[8], // founded
                    parts[5].toFloat(), // lat
                    parts[6].toFloat() // lon
                )
                cities.add(city)
            }
            cities.sortBy { it.title }
        }
    }
}

class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvName: TextView
    var tvRegion: TextView

    init {
        tvName = itemView.findViewById(R.id.tvCity)
        tvRegion = itemView.findViewById(R.id.tvRegion)
    }
}

class CityAdapter(val cities: MutableList<City>) : RecyclerView.Adapter<CityHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.city_list_item, parent, false)

        val holder = CityHolder(view)
        view.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != NO_POSITION) {
                onItemClickListener?.invoke(pos)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.tvName.text = cities[position].title
        holder.tvRegion.text = cities[position].region
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(f: (Int) -> Unit) { onItemClickListener = f }
}

class CitiesList : Fragment() {

    lateinit var list: RecyclerView

    lateinit var state: android.os.Parcelable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cities_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.initCities(requireContext())

        val adapter = CityAdapter(Common.cities)

        adapter.setOnItemClickListener {
            setFragmentResult("chosen_city", bundleOf("city" to Common.cities[it]))
        }

        list = view.findViewById(R.id.rvCityList)
        list.adapter = adapter
    }

    override fun onPause() {
        super.onPause()

        state = (list.layoutManager as LinearLayoutManager).onSaveInstanceState()!!
    }
}