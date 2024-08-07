package com.example.cartes_animees.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cartes_animees.R
import com.example.cartes_animees.model.Animation

class AnimationAdapter(context: Context, animations: List<Animation>) : ArrayAdapter<Animation>(context, 0, animations) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val animation = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_animation, parent, false)

        val tvLibelleAnimation = view.findViewById<TextView>(R.id.tvLibelleAnimation)
        tvLibelleAnimation.text = animation?.libelle_animation

        return view
    }
}
