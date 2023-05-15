package com.example.justmeet.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justmeet.API.CrudApi
import com.example.justmeet.Adapters.AdapterMatches
import com.example.justmeet.Models.User
import com.example.justmeet.Models.listUserMatches
import com.example.justmeet.Models.userLog
import com.example.justmeet.databinding.FragmentComunicationBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private lateinit var binding: FragmentComunicationBinding

class ComunicationFragment : Fragment(), CoroutineScope {
    var job = Job()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComunicationBinding.inflate(inflater, container, false)

        runBlocking {
            val corrutina = launch {
                var crudApi = CrudApi()
                listUserMatches = crudApi.getUsersMatches(userLog!!.idUser!!) as ArrayList<User>
            }
            corrutina.join()
        }
        llansarLlista()

        return binding.root
    }


    private fun llansarLlista() {
        binding.recyclerMatches.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerMatches.adapter = AdapterMatches(listUserMatches)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}