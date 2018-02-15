package ggregor.androidvalidator

/**
 * Created by GG on 30.12.2017.
 */
inline fun <reified T : Any> Any.getThroughReflection(propertyName: String?): T? {

    //return javaClass.kotlin.memberProperties.first { it.name == propertyName }.get(this)
    val getterName = "get" + propertyName?.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        null
    }
}

