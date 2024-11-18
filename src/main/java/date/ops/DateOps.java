package date.ops;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateOps {

	public static void main(String[] args) {
		ZonedDateTime dateTime = ZonedDateTime.parse("2013-09-29T18:46:19Z");
		String dateTimeStr = DateTimeFormatter.ISO_INSTANT.format(dateTime);
		System.out.println(dateTimeStr);
		ZonedDateTime utcDateTime = ZonedDateTime.parse(dateTimeStr);
		System.out.println(utcDateTime);
		ZonedDateTime utcTomorrow = utcDateTime.plusDays(1);
		LocalDate tomorrowDate = utcTomorrow.toLocalDate();
		LocalDate localDate = LocalDate.now(ZoneId.of("UTC"));
		System.out.println(tomorrowDate.isAfter(localDate));
		

		
		 localDate = LocalDate.parse("2023-05-09",DateTimeFormatter.ISO_DATE);
				 System.out.println(localDate);
				LocalTime localTime = LocalTime.of(0,0);
				dateTime = ZonedDateTime.of(localDate,localTime,ZoneId.of("UTC"));
				System.out.println(dateTime);
	}

}
