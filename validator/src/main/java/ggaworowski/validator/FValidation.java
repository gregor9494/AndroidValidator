package ggaworowski.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ggaworowski.validator.ValidatorConst.NO_MESSAGE_VALUE;
import static ggaworowski.validator.ValidatorConst.NO_VALIDATION_VALUE;


/**
 * Created by GG on 05.02.2018.
 * <p>
 * You can assign annotation many times to the same variable but you have to group it with FValidations
 * Using valueBiggerThanAnotherComparable(Smaller) you have to remember that both variables should implements Comparable
 * If you want to specify many errors or messages you can simply use annotation many times for the same variable
 * Variables have to have proper (ex.'getName()') getters
 */


@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(FValidations.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FValidation {
    FieldTypes type() default FieldTypes.NONE;

    DateValidation dateValidation() default DateValidation.NONE;

    String valueBiggerThanAnotherComparable() default "";

    String valueSmallerThanAnotherComparable() default "";

    String regexPattern() default "";

    int maxLength() default NO_VALIDATION_VALUE;

    int minLength() default NO_VALIDATION_VALUE;

    long maxValue() default NO_VALIDATION_VALUE;

    long minValue() default NO_VALIDATION_VALUE;

    int errorMessageRes() default NO_MESSAGE_VALUE;

    boolean hasToBeNotNull() default false;

    String errorMessageText() default "";

}
