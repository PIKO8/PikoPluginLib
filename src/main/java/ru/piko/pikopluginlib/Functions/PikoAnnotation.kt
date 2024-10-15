package ru.piko.pikopluginlib.Functions

import kotlin.reflect.KFunction

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPEALIAS
)
@Retention(AnnotationRetention.SOURCE)
annotation class NotRecommended(
    val reason: String
)
