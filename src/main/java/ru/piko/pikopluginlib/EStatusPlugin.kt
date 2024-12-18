package ru.piko.pikopluginlib;

public enum EStatusPlugin {
    BLOCKED,
    ENABLE,
    DISABLE;

    public boolean isEnable() { return this == BLOCKED || this == ENABLE; }
    public boolean isDisable() { return this == DISABLE; }
    public boolean isBlocked() { return this == BLOCKED; }
    public boolean isUnavailable() { return this == DISABLE || this == BLOCKED; }

}
