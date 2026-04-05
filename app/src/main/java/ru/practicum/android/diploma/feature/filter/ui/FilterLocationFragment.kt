package ru.practicum.android.diploma.feature.filter.ui

import android.os.Bundle
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterLocationBinding
import ru.practicum.android.diploma.util.getNavController

class FilterLocationFragment : Fragment() {

    private var _binding: FragmentFilterLocationBinding? = null
    private val binding get() = _binding!!
    private var countryTextWatcher: TextWatcher? = null
    private var regionTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        //TODO: Получить фильтры из SharedPreferences
        //TODO: Если есть регион, но нет страны, то сохранить страну
        //TODO: Если регион или страна есть, то показать кнопку Выбрать
        binding.countryInput.setText("")
        binding.regionInput.setText("Регион")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countryTextWatcher?.let { binding.countryInput.removeTextChangedListener(it) }
        regionTextWatcher?.let { binding.regionInput.removeTextChangedListener(it) }
        countryTextWatcher = null
        regionTextWatcher = null
        binding.countryAction.setOnClickListener(null)
        binding.regionAction.setOnClickListener(null)
        _binding = null
    }

    private fun setupListeners() {
        binding.countryTextLayout.setOnClickListener {
            navigateToCountry()
        }
        binding.countryInput.setOnClickListener {
            navigateToCountry()
        }
        binding.regionTextLayout.setOnClickListener {
            navigateToRegion()
        }
        binding.regionInput.setOnClickListener {
            navigateToRegion()
        }
        binding.countryAction.setOnClickListener(
            onActionButtonClick(binding.countryInput, ::navigateToCountry)
        )
        binding.regionAction.setOnClickListener(
            onActionButtonClick(binding.regionInput, ::navigateToRegion)
        )
        binding.selectButton.setOnClickListener {
            requireActivity().getNavController(R.id.containerView).navigateUp()
        }
        countryTextWatcher =
            binding.countryInput.addTextChangedListener(
                onTextChanged = onFilterTextChanged(
                    binding.countryTextLayout,
                    binding.countryAction
                )
            )
        regionTextWatcher =
            binding.regionInput.addTextChangedListener(
                onTextChanged = onFilterTextChanged(
                    binding.regionTextLayout,
                    binding.regionAction
                )
            )
    }

    private fun navigateToCountry() {
        requireActivity().getNavController(R.id.containerView)
            .navigate(R.id.action_filterLocationFragment_to_filterCountryFragment)
    }

    private fun navigateToRegion() {
        requireActivity().getNavController(R.id.containerView)
            .navigate(R.id.action_filterLocationFragment_to_filterRegionFragment)
    }

    private fun onFilterTextChanged(
        textLayout: TextInputLayout,
        actionButton: ImageButton
    ): (CharSequence?, Int, Int, Int) -> Unit {
        return { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                textLayout.defaultHintTextColor = ContextCompat.getColorStateList(requireActivity(), R.color.gray)
                actionButton.setImageResource(R.drawable.ic_arrow_forward)
            } else {
                val typedValue = TypedValue()
                requireActivity().theme.resolveAttribute(
                    R.attr.colorOnBackgroundPrimary,
                    typedValue,
                    true
                )
                textLayout.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireActivity(), typedValue.resourceId)
                actionButton.setImageResource(R.drawable.ic_close)
            }
        }
    }

    private fun onActionButtonClick(textInput: TextInputEditText, navigateAction: ()-> Unit): View.OnClickListener {
        return {
            if (textInput.text.isNullOrEmpty()) {
                navigateAction.invoke()
            } else {
                //TODO: удалить фильтр
            }
        }
    }
}
