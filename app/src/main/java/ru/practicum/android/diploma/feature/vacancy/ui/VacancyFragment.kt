package ru.practicum.android.diploma.feature.vacancy.ui

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.feature.vacancy.domain.model.Contacts
import ru.practicum.android.diploma.feature.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.feature.vacancy.presentation.VacancyState
import ru.practicum.android.diploma.feature.vacancy.presentation.VacancyViewModel
import ru.practicum.android.diploma.feature.vacancy.ui.model.PhoneInfo
import ru.practicum.android.diploma.util.fromDpToPx
import ru.practicum.android.diploma.util.ui.DescriptionFormatterVacancy
import ru.practicum.android.diploma.util.ui.SalaryFormatterVacancy

class VacancyFragment : Fragment() {

    private val vacancyViewModel by viewModel<VacancyViewModel>()

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private var contentVacancy: VacancyDetail? = null

    private val args by navArgs<VacancyFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // можно потестить захардкодить разные id
        val id = args.vacancyId
        vacancyViewModel.getVacancyDetail(id)
        vacancyViewModel.observeVacancyDetail().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.share.setOnClickListener {
            contentVacancy?.url?.let { url ->
                vacancyViewModel.sendVacancyViaMessenger(url)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: VacancyState) {
        when (state) {
            is VacancyState.Content -> showContent(state)
            is VacancyState.Error -> showError(state)
            is VacancyState.Loading -> showLoading()
            is VacancyState.Empty -> showEmpty(state)
        }
    }

    private fun showEmpty(content: VacancyState.Empty) {
        with(binding) {
            progressBar.visibility = View.GONE
            vacancyLayout.visibility = View.GONE
            share.visibility = View.GONE
            addToFavorites.visibility = View.GONE
            placeHolderForEmpty.visibility = View.VISIBLE
            emptyTv.setText(content.message)
        }
    }

    private fun showContent(content: VacancyState.Content) {
        val vacancy = content.content
        contentVacancy = vacancy
        with(binding) {
            placeHolderForEmpty.visibility = View.INVISIBLE
            share.visibility = View.VISIBLE
            addToFavorites.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            vacancyLayout.visibility = View.VISIBLE
            serverError.visibility = View.GONE
            noInternetPlaceHolder.visibility = View.GONE

            setupMainInfo(vacancy)
            setupSkills(vacancy.skills)
            setupContacts(vacancy)
        }
    }

    private fun setupMainInfo(vacancy: VacancyDetail) {
        with(binding) {
            vacancyName.text = vacancy.name
            vacancySalary.text = SalaryFormatterVacancy(vacancy.salary, requireContext()).format()

            employerName.text = vacancy.employer?.name
            regionCity.text = vacancy.address?.city ?: vacancy.area?.name

            Glide.with(requireContext())
                .load(vacancy.employer?.logo)
                .placeholder(R.drawable.ic_placeholder)
                .into(employerLogo)

            requiredExperience.text = vacancy.experience?.name
            val scheduleText = "${vacancy.employment?.name}, ${vacancy.schedule?.name}"
            employmentSchedule.text = scheduleText
            // нужна функция форматирования текста?
            vacancyDescription.text = DescriptionFormatterVacancy(vacancy.description).format()
        }
    }

    private fun setupSkills(skills: List<String>?) {
        if (skills.isNullOrEmpty()) {
            binding.skillsGroup.visibility = View.GONE
        } else {
            binding.skillsGroup.visibility = View.VISIBLE
            binding.vacancySkills.text = skills.joinToString("\n") { " • $it" }
        }
    }

    private fun setupContacts(vacancy: VacancyDetail) {
        val contacts = vacancy.contacts
        val isContactsEmpty = contacts == null ||
            contacts.name.isNullOrEmpty() &&
            contacts.email.isNullOrEmpty() &&
            contacts.phones?.isEmpty() == true

        if (isContactsEmpty) {
            binding.contactGroup.visibility = View.GONE
        } else {
            binding.contactGroup.visibility = View.VISIBLE
            displayContactData(contacts)
        }
    }

    private fun displayContactData(contacts: Contacts?) {
        if (contacts == null) return

        setupContactName(contacts.name)
        setupContactEmail(contacts.email)

        if (!contacts.phones.isNullOrEmpty()) {
            val phoneInfos = contacts.phones.map {
                PhoneInfo(it.comment, it.formatted ?: "")
            }
            onPhoneInfoReceived(phoneInfos)
        }
    }

    private fun setupContactName(name: String?) {
        binding.vacancyContactName.apply {
            text = name
            visibility = if (name.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun setupContactEmail(email: String?) {
        binding.vacancyContactEmail.apply {
            text = email
            visibility = if (email.isNullOrEmpty()) View.GONE else View.VISIBLE
            setOnClickListener {
                email?.let { vacancyViewModel.selectEmailClientAndSend(it) }
            }
        }
    }

    private fun showError(error: VacancyState.Error) {
        val isNoInternet = error.errorMessage == getString(R.string.no_internet)
        with(binding) {
            placeHolderForEmpty.visibility = View.INVISIBLE
            progressBar.visibility = View.GONE
            share.visibility = View.GONE
            addToFavorites.visibility = View.GONE
            vacancyLayout.visibility = View.GONE
            noInternetPlaceHolder.visibility = if (isNoInternet) View.VISIBLE else View.GONE
            serverError.visibility = if (isNoInternet) View.GONE else View.VISIBLE
        }
    }

    private fun showLoading() {
        with(binding) {
            placeHolderForEmpty.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            vacancyLayout.visibility = View.GONE
            share.visibility = View.GONE
            addToFavorites.visibility = View.GONE
        }
    }

    private fun onPhoneInfoReceived(phones: List<PhoneInfo>) {
        if (phones.isNotEmpty()) {
            var upperId = binding.vacancyContactEmail.id
            for (phone in phones) {
                if (phone.comment != null) {
                    upperId = initPhoneView(
                        MaterialTextView(getPhoneCommentContextThemeWrapper()),
                        upperId,
                        phone.comment
                    )
                }
                val phoneTextView = MaterialTextView(getPhoneContextThemeWrapper())
                phoneTextView.setOnClickListener { vacancyViewModel.showCallAppsAndDial(phone.phone) }
                upperId = initPhoneView(phoneTextView, upperId, phone.phone)
            }
        }
    }

    private fun initPhoneView(view: TextView, upperId: Int, text: String): Int {
        val generatedId = View.generateViewId()
        view.apply {
            id = generatedId
            this.text = text
            layoutParams = getPhoneLayoutParams(upperId)
        }
        binding.vacancyLayout.addView(view)
        return generatedId
    }

    private fun getPhoneLayoutParams(upperId: Int): ConstraintLayout.LayoutParams =
        ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topToBottom = upperId
            topMargin = PHONE_VIEW_TOP_MARGIN.fromDpToPx(requireActivity())
            startToEnd = binding.start16Line.id
            endToStart = binding.end16Line.id
            horizontalBias = HORIZONTAL_BIAS
        }

    private fun getPhoneContextThemeWrapper() = ContextThemeWrapper(
        requireContext(),
        R.style.VacancyContactHighlightTextViewStyle
    )

    private fun getPhoneCommentContextThemeWrapper() = ContextThemeWrapper(
        requireContext(),
        R.style.VacancyDetailsSingleTextViewStyle
    )

    companion object {
        private const val PHONE_VIEW_TOP_MARGIN = 8
        private const val HORIZONTAL_BIAS = 0f
    }
}
