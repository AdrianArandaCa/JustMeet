package com.example.justmeet.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justmeet.R
import com.example.justmeet.databinding.FragmentPlayBinding
import com.example.justmeet.databinding.FragmentPremiumBinding

private lateinit var binding: FragmentPremiumBinding
class PremiumFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPremiumBinding.inflate(inflater, container, false)


        binding.cardViewPremium1.setOnClickListener {
            binding.linearOpcion1.setBackgroundResource(R.drawable.fondogold_selecteduno)
            binding.tvMesPremium.setTextColor(resources.getColor(R.color.black))
            binding.tvPrecioPremium.setTextColor(resources.getColor(R.color.black))

            binding.linearOpcion2.setBackgroundResource(R.drawable.fondobg3)
            binding.tvMesPremium1.setTextColor(resources.getColor(R.color.light_gold))
            binding.tvPrecioPremium1.setTextColor(resources.getColor(R.color.light_gold))
            binding.cardViewPremium2.isSelected = false

        }
        binding.cardViewPremium2.setOnClickListener {

            binding.linearOpcion2.setBackgroundResource(R.drawable.fondogold_selecteduno)
            binding.tvMesPremium1.setTextColor(resources.getColor(R.color.black))
            binding.tvPrecioPremium1.setTextColor(resources.getColor(R.color.black))

            binding.linearOpcion1.setBackgroundResource(R.drawable.fondobg3)
            binding.tvMesPremium.setTextColor(resources.getColor(R.color.light_gold))
            binding.tvPrecioPremium.setTextColor(resources.getColor(R.color.light_gold))
            binding.cardViewPremium1.isSelected = false
        }
        return binding.root
    }
}