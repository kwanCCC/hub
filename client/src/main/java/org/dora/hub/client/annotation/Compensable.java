package org.dora.hub.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SDE on 2018/6/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compensable {
    /**
     * @return
     */
    int retries() default 0;

    /**
     * Compensate method name
     *
     * @return
     */
    String compensation() default "";

    /**
     * N * compensation timeout should be less than @link: @Transaction timeout
     */
    int timeout() default -1;
}
