package org.dromara.mybatis.jpa.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies a field of an entity that holds the partition key of a table.
 *
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PartitionKey {

}
