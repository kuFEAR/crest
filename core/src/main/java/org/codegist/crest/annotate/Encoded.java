package org.codegist.crest.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value={PARAMETER,METHOD,FIELD,CONSTRUCTOR,TYPE})
@Retention(value=RUNTIME)
public @interface Encoded {

}
