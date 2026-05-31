package ru.practicum.android.diploma.util.ui

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.BulletSpan
import android.text.style.MetricAffectingSpan
import ru.practicum.android.diploma.feature.vacancy.domain.model.VacancyFormattedDescription

class VacancyFormatter {
    fun formatDescription(formattedDescription: VacancyFormattedDescription, headerTypeFace: Typeface): CharSequence {
        val spannableDescription = SpannableString(formattedDescription.description)
        for (formatRange in formattedDescription.formatRanges) {
            val spannable = when (formatRange.first) {
                VacancyFormattedDescription.TextFormatType.HEADER -> CustomTypefaceSpan(headerTypeFace)
                VacancyFormattedDescription.TextFormatType.LIST_ITEM -> BulletSpan(SPAN_BULLET_GAP_WIDTH)
            }
            spannableDescription.setSpan(
                spannable,
                formatRange.second.first,
                formatRange.second.last,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableDescription
    }

    fun formatSkills(skills: List<String>): CharSequence {
        val spanRanges = mutableListOf<IntRange>()
        var appendedSkills = ""
        var i = 0
        skills.forEachIndexed { index, skill ->
            spanRanges.add(IntRange(i, i + skill.length))
            i += if (index == skills.lastIndex) {
                skill.length
            } else {
                skill.length + 1
            }
            appendedSkills += skill
            if (index != skills.lastIndex) {
                appendedSkills += "\n"
            }
        }
        val spannableSkills = SpannableString(appendedSkills)
        spanRanges.forEach { spanRange ->
            val spannable = BulletSpan(SPAN_BULLET_GAP_WIDTH)
            spannableSkills.setSpan(spannable, spanRange.first, spanRange.last, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableSkills
    }

    private class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.typeface = typeface
        }

        override fun updateMeasureState(ms: TextPaint) {
            ms.typeface = typeface
        }
    }

    companion object {
        private const val SPAN_BULLET_GAP_WIDTH = 16
    }
}
