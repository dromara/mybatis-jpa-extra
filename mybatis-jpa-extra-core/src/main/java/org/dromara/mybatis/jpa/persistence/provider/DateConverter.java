package org.dromara.mybatis.jpa.persistence.provider;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.dromara.mybatis.jpa.persistence.FieldColumnMapper;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.TemporalType;

public class DateConverter{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(DateConverter.class);
	
	public static  String convert(Object entity ,FieldColumnMapper fieldColumnMapper,boolean isUpdate) {
		String dateValue = "";
		if(isUpdate) {
			return convertDateTime(LocalDateTime.now(),fieldColumnMapper);
		}
		if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")) {
			if(BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == null
					|| BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == "") {
				dateValue = convertDateTime(LocalDateTime.now(),fieldColumnMapper);
			}else {
				dateValue = BeanUtil.get(entity, fieldColumnMapper.getFieldName()).toString();
			}
		}else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("Date")) {
			Date date =(Date)BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			if(date == null) {
				dateValue = convertDateTime(LocalDateTime.now(),fieldColumnMapper);
			}else {
				if(fieldColumnMapper.getTemporalAnnotation().value() == TemporalType.TIMESTAMP) {
					dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				}else if(fieldColumnMapper.getTemporalAnnotation().value() == TemporalType.DATE) {
					dateValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
				}else if(fieldColumnMapper.getTemporalAnnotation().value() == TemporalType.TIME) {
					dateValue = new SimpleDateFormat("HH:mm:ss").format(date);
				}
			}
		}else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalDate")) {
			LocalDate localDate =(LocalDate)BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			if(localDate == null) {
				localDate = LocalDate.now();
			}
			dateValue = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalTime")) {
			LocalTime localTime =(LocalTime)BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			if(localTime == null) {
				localTime = LocalTime.now();
			}
			dateValue = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		}else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("LocalDateTime")) {
			LocalDateTime localDateTime =(LocalDateTime)BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			if(localDateTime == null) {
				localDateTime = LocalDateTime.now();
			}
			dateValue = convertDateTime(localDateTime,fieldColumnMapper);
		}
		_logger.debug("Date Value {}" ,dateValue);
		return dateValue;
	}
	
	public static String convertDateTime(LocalDateTime localDateTime ,FieldColumnMapper fieldColumnMapper) {
		if(fieldColumnMapper.getTemporalAnnotation().value() == TemporalType.TIMESTAMP) {
			return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}else if(fieldColumnMapper.getTemporalAnnotation().value() == TemporalType.DATE) {
			return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}else{
			return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		}
	}
}
