package ggaworowski.justkotlinapplication.utils.validator

import android.text.TextUtils

import ggregor.androidvalidator.*
import java.lang.reflect.Field
import java.util.*
import java.util.regex.Pattern
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Created by GG on 29.12.2017.
 */
class Validator {

    companion object {
        val NO_VALIDATION_VALUE = -1

        fun validate(obj: Any, callback: (ValidatorError) -> Unit = {}): Boolean {
            obj::class.java.declaredFields.forEach {
                val field = it
                it.declaredAnnotations.forEach({
                    if (it is FValidation) {
                        if (validateByAnnotation(it, obj, field, callback)) return false
                    } else if (it is FValidations) {
                        val annotations = it.value
                        annotations.forEach {
                            if (validateByAnnotation(it, obj, field, callback)) return false
                        }
                    }
                })
            }

            return true
        }

        private fun validateByAnnotation(it: FValidation, obj: Any, field: Field, callback: (ValidatorError) -> Unit): Boolean {
            if (it != null && !validateByType(it, obj.getThroughReflection(field.name))) {
                sendMessage(it, callback)
                return true
            }
            return false
        }

        private fun validateByType(annotation: FValidation, obj: Any?): Boolean {

            if (annotation.type == FieldTypes.EMAIL) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(obj.toString()).matches()) {
                    return false
                }
            }

            if (annotation.type == FieldTypes.NUMBER) {
                if (!TextUtils.isDigitsOnly(obj.toString())) {
                    return false
                }
            }

            if (annotation.type == FieldTypes.PHONE) {
                if (!android.util.Patterns.PHONE.matcher(obj.toString()).matches()) {
                    return false
                }
            }

            if (annotation.type == FieldTypes.DATE) {
                if (obj == null && annotation.dateValidation == DateValidation.NOT_EMPTY) return false
                if (obj !is Date) return false
                if (annotation.dateValidation == DateValidation.AFTER_NOW && Date().after(obj)) return false
                if (annotation.dateValidation == DateValidation.BEFORE_NOW && Date().before(obj)) return false
            }

            return validateField(annotation, obj)
        }

        private fun validateField(annotation: FValidation, obj: Any?): Boolean {
            if(annotation.hasToBeNotNull && obj == null) return false

            if(annotation.regexPattern.isNotEmpty() && obj?.toString()?.isNotEmpty() == true){
                if (!Pattern.matches(annotation.regexPattern,obj.toString())) {
                    return false
                }
            }

            if (obj?.toString()?.length ?: 0 < annotation.minLength && annotation.minLength != NO_VALIDATION_VALUE) {
                return false
            }

            if (obj?.toString()?.length ?: 0 > annotation.maxLength && annotation.maxLength != NO_VALIDATION_VALUE) {
                return false
            }

            if (annotation.maxValue != NO_VALIDATION_VALUE.toLong()) {
                TextUtils.isDigitsOnly(obj.toString())
                val number = obj.toString().toDouble()
                if (number > annotation.maxValue) {
                    return false
                }
            }


            if (annotation.minValue != NO_VALIDATION_VALUE.toLong()) {
                TextUtils.isDigitsOnly(obj.toString())
                val number = obj.toString().toDouble()
                if (number < annotation.minValue) {
                    return false
                }
            }

            return true
        }

        private fun sendMessage(annotation: FValidation, callback: (ValidatorError) -> Unit) {
            callback.invoke(ValidatorError(annotation.errorMessageRes, annotation.errorMessageText))
        }
    }

}