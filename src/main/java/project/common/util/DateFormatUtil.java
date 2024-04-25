package project.common.util;

import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatUtil {
    //6자리 문자열로 월까지만 입력받은 날짜 문자열을 해당 월의 첫 날 00시 00분의 LocalDateTime으로 변환하는 메서드
    //ex) "202401" -> 2024-01-01 00:00:00
    public static LocalDateTime parseStartOfMonth(String dateStr){
        try {
            dateStr = dateStr + "01";
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new PlantException(ErrorCode.DATE_INVALID);
        }
    }
    //8자리 문자열로 입력받은 날짜 문자열을 해당일의 00시 00분의 LocalDateTime으로 변환하는 메서드
    //ex) "20240521" -> 2024-05-21 00:00:00
    public static LocalDateTime parseStartOfDay(String dateStr){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(dateStr, formatter).atStartOfDay();
        }
        catch (DateTimeParseException e){
            throw new PlantException(ErrorCode.DATE_INVALID);
        }
    }
}
