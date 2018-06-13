package org.dora.hub.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by <-chan 666 on 2018/6/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionStart {

    /**
     * timeout() < 0 mean never timeout
     *
     * @return
     */
    int timeout() default -1;
}
