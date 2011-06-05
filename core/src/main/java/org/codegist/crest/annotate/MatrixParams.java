package org.codegist.crest.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * {@link HeaderParam} aggregator for interface and method levels, to be used when more than one default header need to be added.
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface MatrixParams {

    MatrixParam[] value();

}
