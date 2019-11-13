package net.dimterex.sync_client.ui.formatter

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    fun format(date: Date) : String = SimpleDateFormat(
        DATE_PRESENTATION_PATTERN, Locale.getDefault()
    ).format(date)

    companion object {

        const val DATE_PRESENTATION_PATTERN = "dd.MM.yyyy"
    }
}