package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.justmeet.Activitys.ActivityBuyPremium
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.FragmentPremiumBinding

private lateinit var binding: FragmentPremiumBinding

class PremiumFragment : Fragment() {
    var isSelected: Boolean = false

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
            binding.cardViewPremium1.isSelected = true
            binding.linearOpcion1.setBackgroundResource(R.drawable.fondogold_selecteduno)
            binding.tvMesPremium.setTextColor(resources.getColor(R.color.black))
            binding.tvPrecioPremium.setTextColor(resources.getColor(R.color.black))

            binding.linearOpcion2.setBackgroundResource(R.drawable.fondobg3)
            binding.tvMesPremium1.setTextColor(resources.getColor(R.color.light_gold))
            binding.tvPrecioPremium1.setTextColor(resources.getColor(R.color.light_gold))
            binding.cardViewPremium2.isSelected = false
        }

        binding.cardViewPremium2.setOnClickListener {
            binding.cardViewPremium2.isSelected = true
            binding.linearOpcion2.setBackgroundResource(R.drawable.fondogold_selecteduno)
            binding.tvMesPremium1.setTextColor(resources.getColor(R.color.black))
            binding.tvPrecioPremium1.setTextColor(resources.getColor(R.color.black))

            binding.linearOpcion1.setBackgroundResource(R.drawable.fondobg3)
            binding.tvMesPremium.setTextColor(resources.getColor(R.color.light_gold))
            binding.tvPrecioPremium.setTextColor(resources.getColor(R.color.light_gold))
            binding.cardViewPremium1.isSelected = false
        }

        binding.btnBuyPremium.setOnClickListener {
            if (userLog!!.premium!!) {
                Toast.makeText(requireContext(), "Ya tienes el premium comprado", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (!binding.cardViewPremium1.isSelected && !binding.cardViewPremium2.isSelected) {
                    Toast.makeText(
                        requireContext(),
                        "Tienes que seleccionar un plan!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (binding.cardViewPremium1.isSelected) {
                        val intento = Intent(requireContext(), ActivityBuyPremium::class.java)
                        intento.putExtra("Price", binding.tvPrecioPremium.text.toString())
                        intento.putExtra("Month", binding.tvMesPremium.text.toString())
                        startActivity(intento)
                    } else if (binding.cardViewPremium2.isSelected) {
                        val intento = Intent(requireContext(), ActivityBuyPremium::class.java)
                        intento.putExtra("Price", binding.tvPrecioPremium1.text.toString())
                        intento.putExtra("Month", binding.tvMesPremium1.text.toString())
                        startActivity(intento)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // updateData()
    }

    fun updateData() {
        if (userLog!!.premium!!) {
            binding.btnBuyPremium.isEnabled = false
        }
    }
}