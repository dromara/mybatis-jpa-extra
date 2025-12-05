package org.dromara.mybatis.jpa;

import java.time.LocalDateTime;

import org.apache.commons.lang3.SystemUtils;

public class MybatisJpa {

    public static String version() {
        return 
                String.format("""
                    ---------------------------------------------------------------------------------
                    -              JAVA    
                    -              %s java version %s, class %s
                    -              %s (build %s, %s)
                    ---------------------------------------------------------------------------------
                    -                                MyBatis JPA Extra 
                    -                                                        
                    -              %sCopyright 2018 - %s https://gitee.com/dromara/mybatis-jpa-extra/
                    -
                    -              Licensed under the Apache License, Version 2.0 
                    ---------------------------------------------------------------------------------
                    """,
                    SystemUtils.JAVA_VENDOR,
                    SystemUtils.JAVA_VERSION,
                    SystemUtils.JAVA_CLASS_VERSION,
                    SystemUtils.JAVA_VM_NAME,
                    SystemUtils.JAVA_VM_VERSION,
                    SystemUtils.JAVA_VM_INFO,
                    (char)0xA9,
                    LocalDateTime.now().getYear()
                );
    }
    
    
}
