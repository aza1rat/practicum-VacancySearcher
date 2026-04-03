package ru.practicum.android.diploma.util.ui

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.text.style.StyleSpan
import android.util.Log

class DescriptionFormatterVacancy(private val description: String?) {

    fun format(): CharSequence {
        if (description.isNullOrBlank()) return ""

        val builder = SpannableStringBuilder()
        val blocks = description.split(Regex("(\n\n)+"))

        blocks.forEachIndexed { index, block ->
            val lines = block.trim().split("\n")

            lines.forEach { line ->
                val trimmedLine = line.trim()
                if (trimmedLine.isEmpty()) return@forEach

                val start = builder.length

                if (trimmedLine.endsWith(":") && trimmedLine.length < 100) {
                    builder.append(trimmedLine)
                    builder.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.append("\n")
                } else {
                    builder.append(trimmedLine)

                    if (lines.size > 1) {
                        // Используем константу для точности
                        val gapWidth = 40

                        builder.setSpan(
                            BulletSpan(20), // 20 - это стандартный отступ точки
                            start,
                            builder.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        builder.setSpan(
                            LeadingMarginSpan.Standard(0),
                            start,
                            builder.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    builder.append("\n")
                }
            }

            if (index < blocks.size - 1) {
                builder.append("\n")
            }
        }

        return builder.trimEnd()
    }
}
