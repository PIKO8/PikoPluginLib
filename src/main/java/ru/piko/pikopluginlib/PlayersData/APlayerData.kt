package ru.piko.pikopluginlib.PlayersData

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.lang.reflect.Field
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

abstract class APlayerData {
    
    abstract val id: String
    
//    val offlinePlayer: OfflinePlayer
//        get() = data.offlineOwner
//
//    val player: Player?
//        get() = data.owner
    
    fun <T : APlayerData> to(dataClass: Class<T>): T {
        return if (this::class.java == dataClass) {
            this as T
        } else {
            throw UnsupportedOperationException("Cannot convert to ${dataClass.name}")
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <T> getVariable(name: String, type: Class<T>): T? {
        return try {
            val field: Field = this::class.java.getDeclaredField(name)
            field.isAccessible = true
            field.get(this) as T
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            null
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <T> setVariable(name: String, value: T) {
        try {
            val field: Field = this::class.java.getDeclaredField(name)
            field.isAccessible = true
            field.set(this, value)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}