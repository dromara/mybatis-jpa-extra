package org.dromara.mybatis.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * @see org.dromara.mybatis.jpa.annotations.SoftDelete
 */
@Target( {FIELD, METHOD} )
@Retention( RUNTIME )
public @interface SoftDelete {
	/**
	 * @return a SQL expression that evaluates to the default column value
	 */
	String value()  default "1" ;
	
	String delete() default "9" ;
	
}