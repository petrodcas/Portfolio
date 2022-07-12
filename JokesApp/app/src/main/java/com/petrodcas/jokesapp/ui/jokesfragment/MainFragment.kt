package com.petrodcas.jokesapp.ui.jokesfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.petrodcas.jokesapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel.errorEvent.observe(viewLifecycleOwner) { stringID ->
                Snackbar.make(requireView(), stringID, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.jokeText.observe(viewLifecycleOwner) { jokeString ->
            binding.textviewFirst.text = jokeString
        }

        viewModel.loadingEvent.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.textviewFirst.text = ""
            }
            binding.loadingScreen.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        binding.parentLayout.setOnClickListener { viewModel.getNextJoke() }
        viewModel.initializeJoke()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}