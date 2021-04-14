package ru.javawebinar.topjava.web.converter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class DateTimeFormatters {
    public static class LocalDateFormatter implements Formatter<LocalDate> {

        @Override
        public LocalDate parse(String text, Locale locale) {
            return parseLocalDate(text);
        }

        @Override
        public String print(LocalDate lt, Locale locale) {
            return lt.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static class LocalTimeFormatter implements Formatter<LocalTime> {

        @Override
        public LocalTime parse(String text, Locale locale) {
            return parseLocalTime(text);
        }

        @Override
        public String print(LocalTime lt, Locale locale) {
            return lt.format(DateTimeFormatter.ISO_LOCAL_TIME);
        }
    }

    public static class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

        @Override
        public LocalDateTime parse(String s, Locale locale) throws ParseException {
            String[] elementsOfS = s.split("T");
            Integer[] elementsOfDate = parseDateOrTimeStringBySymbol(elementsOfS[0], "-");
            Integer[] elementsOfTime = parseDateOrTimeStringBySymbol(elementsOfS[1], ":");
            LocalDate localDate = LocalDate.of(elementsOfDate[0], elementsOfDate[1], elementsOfDate[2]);
            LocalTime localTime = LocalTime.of(elementsOfTime[0], elementsOfTime[1], elementsOfTime[2]);
            return LocalDateTime.of(localDate, localTime);
        }

        @Override
        public String print(LocalDateTime localDateTime, Locale locale) {
            return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        private Integer[] parseDateOrTimeStringBySymbol(String s, String symbol) {
            String[] elementsOfLocalDateOrTimePart = s.split(symbol);
            int length = elementsOfLocalDateOrTimePart.length;
            Integer[] parts = new Integer[length];
            for (int i = 0; i < length; i++) {
                parts[i] = Integer.parseInt(elementsOfLocalDateOrTimePart[i]);
            }
            return parts;
        }
    }
}
