package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.justmeet.Activitys.ActivityJustEditProfile
import com.example.justmeet.Activitys.SettingsActivity
import com.example.justmeet.Models.userLog
import com.example.justmeet.R
import com.example.justmeet.databinding.FragmentPlayBinding
import com.example.justmeet.databinding.FragmentProfileBinding

private lateinit var binding : FragmentProfileBinding
class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        binding.ivProfile.setImageResource(userLog!!.photo!!)
        binding.tvNomEdad.setText("${userLog!!.name!!},${userLog!!.birthday!!}")
        binding.tvSobreMiContent.setText("${userLog!!.description!!}")
        binding.ibSettings.setOnClickListener {
            Toast.makeText(context,"has fet click al buttó de settings",Toast.LENGTH_LONG).show()
            val intento = Intent(context,SettingsActivity::class.java)
            startActivity(intento)
        }

        binding.btnEditarPerfil.setOnClickListener {
            val intento = Intent(context,ActivityJustEditProfile::class.java)
            startActivity(intento)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateData()
    }
    fun updateData() {
        binding.ivProfile.setImageResource(userLog!!.photo!!)
        binding.tvNomEdad.setText("${userLog!!.name!!},${userLog!!.birthday!!}")
        binding.tvSobreMiContent.setText("${userLog!!.description!!}")
    }


}