package ilargia.entitas.codeGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Component {

    String[] pools() default {"Pool"};

    boolean isSingleEntity() default false;

    String customPrefix() default "";

    String[] customComponentName() default {""};

}