package org.dromara.mybatis.jpa.repository.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassParameterizedTypeTest extends ParentClass<JpaEntity,Long>{
    static final  Logger logger = LoggerFactory.getLogger(ClassParameterizedTypeTest.class);
    
    public static void main(String[] args) {
        Class<?> entityClass;
        Class<?> mapperClass = null;
        Type[] pType = ((ParameterizedType) ClassParameterizedTypeTest.class.getGenericSuperclass()).getActualTypeArguments();
        if (pType != null && pType.length >= 2) {
            mapperClass=(Class<?>) pType[0];
            entityClass = (Class<?>) pType[1];
            logger.info("Mapper {} , Entity {}" , String.format(ConstMetadata.LOG_FORMAT, mapperClass.getSimpleName()),entityClass.getSimpleName());
        } else {
            logger.error("invalide initail, need generic type parameter! ");
        }
    }
}
