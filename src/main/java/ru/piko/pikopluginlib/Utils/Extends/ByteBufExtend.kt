package ru.piko.pikopluginlib.Utils.Extends

import io.netty.buffer.ByteBuf
import java.nio.charset.Charset

object ByteBufExtend {
	// Метод расширения для записи строки в ByteBuf
	fun ByteBuf.writeString(str: String, charset: Charset = Charsets.UTF_8): ByteBuf {
		val bytes = str.toByteArray(charset)
		this.writeInt(bytes.size) // Записываем длину строки
		this.writeBytes(bytes) // Записываем сами байты строки
		return this
	}
	
	// Метод расширения для чтения строки из ByteBuf
	fun ByteBuf.readString(charset: Charset = Charsets.UTF_8): String {
		val length = this.readInt() // Читаем длину строки
		val bytes = ByteArray(length)
		this.readBytes(bytes) // Читаем байты строки
		return String(bytes, charset)
	}
	
	// Метод расширения для удобного чтения оставшихся байт в виде строки
	fun ByteBuf.readRemainingString(charset: Charset = Charsets.UTF_8): String {
		val bytes = ByteArray(this.readableBytes())
		this.readBytes(bytes)
		return String(bytes, charset)
	}
}