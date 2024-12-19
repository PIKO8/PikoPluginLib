package ru.piko.pikopluginlib.Utils

object PikoAnnotation {
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
    /**
     * EN: Made by Artificial Intelligence (AI)
     *
     * RU: Сделано Искусственным Интеллектом (ИИ)
     *
     * @param ai - Насколько много сделала нейросеть
     * @param human - Насколько много сделал человек (не считая обращение к нейросети)
     */
    annotation class MadeAI(val ai: Edit, val human: Edit = Edit.False)
    
    enum class Edit {
        False,
        Minimum,
        Medium,
        Many
    }
}

