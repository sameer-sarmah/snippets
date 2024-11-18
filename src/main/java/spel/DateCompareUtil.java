package spel;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import java.time.DateTimeException;
import java.time.ZonedDateTime;

public class DateCompareUtil {

	public static boolean isAfter(String isoDateTimeStringOne, String isoDateTimeStringTwo) {
		try {
			var isoDateTimeOne = ZonedDateTime.parse(isoDateTimeStringOne, ISO_OFFSET_DATE_TIME);
			var isoDateTimeTwo = ZonedDateTime.parse(isoDateTimeStringTwo, ISO_OFFSET_DATE_TIME);
			return isoDateTimeOne.isAfter(isoDateTimeTwo);

		} catch (DateTimeException exception) {

		}
		return false;
	}
	
	/**
	 * Is the first argument timestamp before second argument timestamp
	 * @param isoDateTimeStringOne
	 * @param isoDateTimeStringTwo
	 * @return 
	 */
	public static boolean isBefore(String isoDateTimeStringOne, String isoDateTimeStringTwo) {
		try {
			var isoDateTimeOne = ZonedDateTime.parse(isoDateTimeStringOne, ISO_OFFSET_DATE_TIME);
			var isoDateTimeTwo = ZonedDateTime.parse(isoDateTimeStringTwo, ISO_OFFSET_DATE_TIME);
			return isoDateTimeOne.isBefore(isoDateTimeTwo);

		} catch (DateTimeException exception) {

		}
		return false;
	}

	public static boolean isBetween(String isoDateTimeString, String isoDateTimeStringStart , String isoDateTimeStringEnd) {
		try {
			var isoDateTime = ZonedDateTime.parse(isoDateTimeString, ISO_OFFSET_DATE_TIME);
			var isoDateTimeStart = ZonedDateTime.parse(isoDateTimeStringStart, ISO_OFFSET_DATE_TIME);
			var isoDateTimeEnd = ZonedDateTime.parse(isoDateTimeStringEnd, ISO_OFFSET_DATE_TIME);
			return isoDateTime.isAfter(isoDateTimeStart) && isoDateTime.isBefore(isoDateTimeEnd);

		} catch (DateTimeException exception) {

		}
		return false;
	}
}
