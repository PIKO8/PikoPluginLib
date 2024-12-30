package ru.piko.pikopluginlib.Api

enum class EStatusPlugin {
    BLOCKED_ENABLE,
    BLOCKED_DISABLE,
    ENABLE,
    DISABLE,
    UNREGISTERED;
    
    val isEnable: Boolean
        get() = this == ENABLE || this == BLOCKED_ENABLE
    val isDisable: Boolean
        get() = this == DISABLE || this == BLOCKED_DISABLE
    val isBlocked: Boolean
        get() = this == BLOCKED_ENABLE || this == BLOCKED_DISABLE
    val isUnavailable: Boolean
        get() = this == DISABLE || this == BLOCKED_ENABLE || this == BLOCKED_DISABLE || this == UNREGISTERED
    val isUnregistered: Boolean
        get() = this == UNREGISTERED
}