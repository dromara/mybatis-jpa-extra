package org.dromara.mybatis.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( {FIELD, METHOD} )
@Retention( RUNTIME )
public @interface Encrypted {
	/**
	 * @return Encrypt algorithm
	 */
	String algorithm() default "AES" ;
	
	/**
	 * When true try to use DB encryption rather than local java encryption.
	 */
	boolean dbEncrypt() default false;
	
}