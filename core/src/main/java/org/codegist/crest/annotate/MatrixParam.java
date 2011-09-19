package org.codegist.crest.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>Binds a method parameter to be used as a request URI matrix parameter.</p>
 * <p>Values will be URL encoded unless this is disabled using the {@link org.codegist.crest.annotate.Encoded} annotation.</p>
 * <p>The type of the parameter must either:</p>
 * <ol>
 * <li>Be a primitive</li>
 * <li>Have a toString() method that returns the value to be used</li>
 * <li>Any type being handled by <b>CRest</b>, see available {@link org.codegist.crest.serializer.Serializer} implementations for a list of supported types.</li>
 * <li>Any user specific type given that a {@link org.codegist.crest.serializer.Serializer} has been provided for it.</li>
 * <li>Be a Collection&lt;T&gt;, or an array T[] where T satisfies 2, 3 or 4 above.</li>
 * </ol>
 * <p>Note that for array/Collection, the default behavior will be to create as many pair/value parameters as given values. Values can be merged in one single parameter using the {@link org.codegist.crest.annotate.ListSeparator} annotation</p>
 * <p>When set at interface or method levels, it will add a URI matrix parameter with the given value for all method's to which it applies</p>
 * @see org.codegist.crest.annotate.Serializer
 * @see org.codegist.crest.annotate.ListSeparator
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.PARAMETER})
@Param("MATRIX")
public @interface MatrixParam {

    /**
     * Defines the name of the request URI matrix parameter that will hold the given value
     */
    String value();

    /**
     * Defines the default value of the URI matrix parameter if the value being passed is null
     */
    String defaultValue() default "";
}
