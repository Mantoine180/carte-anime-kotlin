package com.example.cartes_animees.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cartes_animees.R
import com.example.cartes_animees.model.Series

class SeriesAdapter(context: Context, seriesList: List<Series>) : ArrayAdapter<Series>(context, 0, seriesList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val series = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_series, parent, false)

        val tvLibelleSerie = view.findViewById<TextView>(R.id.tvLibelleSerie)
        val tvTheme = view.findViewById<TextView>(R.id.tvTheme)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val tvAnimationCount = view.findViewById<TextView>(R.id.tvAnimationCount)

        tvLibelleSerie.text = series?.libelle_serie
        tvTheme.text = series?.theme
        tvDescription.text = series?.description
        tvAnimationCount.text = "Nombre d'animations : ${series?.animations?.size ?: 0}"

        return view
    }
}
