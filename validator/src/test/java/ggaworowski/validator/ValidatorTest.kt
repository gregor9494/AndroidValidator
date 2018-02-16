package ggaworowski.validator

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.mockito.internal.verification.VerificationModeFactory


/**
 * Created by GG on 15.02.2018.
 */
@RunWith(RobolectricTestRunner::class)
class ValidatorTest {
    @org.junit.Test
    fun testShortName() {
        val objShort = object : Any() {
            @FValidation(minLength = 2, type = FieldTypes.TEXT)
            var shortName = "a"
                internal set
        }

        val objLong = object : Any() {

            @FValidation(minLength = 2, type = FieldTypes.TEXT)
            var longName = "aaaa"
                internal set
        }

        assertFalse(Validator.validate(objShort) { validatorError -> null })
        assertTrue(Validator.validate(objLong) { validatorError -> null })
    }

    @org.junit.Test
    fun testLongName() {
        val objShort = object : Any() {
            @FValidation(maxLength = 2, type = FieldTypes.TEXT)
            var shortName = "a"
                internal set
        }

        val objLong = object : Any() {
            @FValidation(maxLength = 2, type = FieldTypes.TEXT)
            var longName = "aaaa"
                internal set
        }

        assertTrue(Validator.validate(objShort) { validatorError -> null })
        assertFalse(Validator.validate(objLong) { validatorError -> null })
    }

    @org.junit.Test
    fun testEmail() {
        val objEmailShort = object : Any() {
            @FValidation(type = FieldTypes.EMAIL, minLength = 6)
            var shortEmail = "a@a.a"
                internal set
        }

        val objEmail = object : Any() {
            @FValidation(type = FieldTypes.EMAIL)
            var email = "a@a.a"
                internal set
        }

        val objEmailLong = object : Any() {
            @FValidation(type = FieldTypes.EMAIL, maxLength = 6)
            var longEmail = "aaa@aaa.aaa"
                internal set
        }

        assertFalse(Validator.validate(objEmailShort) { validatorError -> null })
        assertTrue(Validator.validate(objEmail) { validatorError -> null })
        assertFalse(Validator.validate(objEmailLong) { validatorError -> null })
    }


    @org.junit.Test
    fun testPhone() {
        val validPhone = object : Any() {
            @FValidation(type = FieldTypes.PHONE, minLength = 6)
            var phone = "123456789"
                internal set
        }

        val validPhone2 = object : Any() {
            @FValidation(type = FieldTypes.PHONE, minLength = 6)
            var phone = "+48123456789"
                internal set
        }

        val invalidPhone = object : Any() {
            @FValidation(type = FieldTypes.PHONE, minLength = 6)
            var phone = "asdasdasdasdas"
                internal set
        }


        assertTrue(Validator.validate(validPhone) { validatorError -> null })
        assertTrue(Validator.validate(validPhone2) { validatorError -> null })
        assertFalse(Validator.validate(invalidPhone) { validatorError -> null })
    }

    @org.junit.Test
    fun testCallback() {
        val samplePhone = object : Any() {
            @FValidation(type = FieldTypes.PHONE, errorMessageText = "test")
            var phone = "asdasd"
        }
        assertFalse(Validator.validate(samplePhone, {
            assertTrue(it.errorText.equals("test"))
        }))

        val samplePhone2 = object : Any() {
            @FValidation(type = FieldTypes.PHONE, errorMessageRes = 1)
            var phone = "asdasd"
        }
        assertFalse(Validator.validate(samplePhone2, {
            assertTrue(it.errorRes == 1)
        }))
    }

    @org.junit.Test
    fun testRegex() {
        val sampleValid = object : Any() {
            @FValidation(type = FieldTypes.TEXT,regexPattern = "/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/")
            var phone = "123456789"
        }
        assertTrue(Validator.validate(sampleValid, {}))


        val sampleInvalid = object : Any() {
            @FValidation(type = FieldTypes.TEXT,regexPattern = "/\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})/")
            var phone = "asdasdasdasdas"
        }
        assertFalse(Validator.validate(sampleInvalid, {}))
    }

}
