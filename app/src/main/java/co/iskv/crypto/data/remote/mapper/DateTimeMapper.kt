package co.iskv.crypto.data.remote.mapper

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import javax.inject.Inject

class DateTimeMapper @Inject constructor() {

    fun map(date: String): DateTime = ISODateTimeFormat.dateTimeParser().withOffsetParsed().parseDateTime(date)

}