package ru.practicum.android.diploma.feature.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.feature.search.presentation.SearchViewModel
import ru.practicum.android.diploma.feature.search.presentation.model.SearchState

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            searchViewModel.onSearchTextChanged(text.toString())
        }
        binding.searchClearIcon.setOnClickListener {
            binding.searchInput.text.clear()
        }
        val adapter = VacanciesAdapter { vacancy ->
            Toast.makeText(requireContext(), vacancy.name, Toast.LENGTH_SHORT).show()
        }
        binding.searchResultsRecyclerView.adapter = adapter
        searchViewModel.observeSearchState().observe(viewLifecycleOwner) { state ->
            if (state != SearchState.Loading)
                binding.mainSearchProgressBar.isVisible = false
            if (state !is SearchState.Result || state != SearchState.EmptyResultError)
                binding.vacanciesCountText.isVisible = false

            when (state) {
                is SearchState.Result -> {
                    binding.searchResultsRecyclerView.isVisible = true
                    adapter.submitList(state.vacancies)
                    binding.vacanciesCountText.isVisible = true
                    binding.vacanciesCountText.text = context?.getString(R.string.vacancies_n_found, state.founded)
                }
                SearchState.EmptyResultError -> TODO()
                SearchState.Idle -> {
                    binding.placeholderImage.isVisible = true
                    binding.placeholderImage.setImageResource(R.drawable.img_search_holder)
                    binding.searchClearIcon.setImageResource(R.drawable.ic_search)
                }
                SearchState.InputStarted -> {
                    binding.placeholderImage.isVisible = false
                    binding.searchClearIcon.setImageResource(R.drawable.ic_close)
                }
                SearchState.Loading -> binding.mainSearchProgressBar.isVisible = true
                SearchState.LoadingMore -> TODO()
                is SearchState.NetworkError -> TODO()
                is SearchState.RequestError -> TODO()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
