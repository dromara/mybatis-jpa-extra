package org.dromara.mybatis.jpa.provider.base;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.TemporalType;

public class DateConverter{    
    static final Logger logger     =     LoggerFactory.getLogger(DateConverter.class);
    
    private static final String TIMESTAMP_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMATTER         = "yyyy-MM-dd";
    private static final String TIME_FORMATTER         = "HH:mm:ss";
            
    public static  String convert(Object entity ,ColumnMapper fieldColumnMapper,boolean isUpdate) {
        String dateValue = "";
        if(fieldColumnMapper.getFieldType().equalsIgnoreCase("Date")) {
            Date date = (Date)BeanUtil.get(entity, fieldColumnMapper.getField());
            if(date == null) {
                dateValue = convertDateTime(LocalDateTime.now(),fieldColumnMapper);
            }else {
                TemporalType temporalType = fieldColumnMapper.getTemporalAnnotation().value();
                if(temporalType == TemporalType.TIMESTAMP) {
                    dateValue = new SimpleDateFormat(TIMESTAMP_FORMATTER).format(date);
                }else if(temporalType == TemporalType.DATE) {
                    dateValue = new SimpleDateFormat(DATE_FORMATTER).format(date);
                }else if(temporalType == TemporalType.TIME) {
                    dateValue = new SimpleDateFormat(TIME_FORMATTER).format(date);
                }
            }
        }else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalDate")) {
            LocalDate localDate =(LocalDate)BeanUtil.get(entity, fieldColumnMapper.getField());
            if(localDate == null) {
                localDate = LocalDate.now();
            }
            dateValue = localDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        }else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalTime")) {
            LocalTime localTime =(LocalTime)BeanUtil.get(entity, fieldColumnMapper.getField());
            if(localTime == null) {
                localTime = LocalTime.now();
            }
            dateValue = localTime.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
        }else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalDateTime")) {
            LocalDateTime localDateTime =(LocalDateTime)BeanUtil.get(entity, fieldColumnMapper.getField());
            if(localDateTime == null) {
                localDateTime = LocalDateTime.now();
            }
            dateValue = convertDateTime(localDateTime,fieldColumnMapper);
        }
        logger.debug("Date Value {}" ,dateValue);
        return dateValue;
    }
    
    public static String convertDateTime(LocalDateTime localDateTime ,ColumnMapper fieldColumnMapper) {
        TemporalType temporalType = fieldColumnMapper.getTemporalAnnotation().value();
        if(temporalType == TemporalType.TIMESTAMP) {
            return localDateTime.format(DateTimeFormatter.ofPattern(TIMESTAMP_FORMATTER));
        }else if(temporalType == TemporalType.DATE) {
            return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        }else{
            return localDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
        }
    }
}
