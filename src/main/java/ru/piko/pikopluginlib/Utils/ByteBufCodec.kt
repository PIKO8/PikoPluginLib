package ru.piko.pikopluginlib.Utils

import io.netty.buffer.ByteBuf
import com.google.common.base.Suppliers
import java.util.function.Supplier
import java.util.function.UnaryOperator

interface ByteBufCodec<V> : ByteBufDecoder<V>, ByteBufEncoder<V> {
	//region Factory methods
	companion object {
		fun <V> of(
			encoder: ByteBufEncoder<V>,
			decoder: ByteBufDecoder<V>
		): ByteBufCodec<V> = object : ByteBufCodec<V> {
			override fun decode(buffer: ByteBuf): V = decoder.decode(buffer)
			override fun encode(buffer: ByteBuf, value: V) = encoder.encode(buffer, value)
		}
		
		fun <V> ofMember(
			encoder: ByteBufMemberEncoder<V>,
			decoder: ByteBufDecoder<V>
		): ByteBufCodec<V> = object : ByteBufCodec<V> {
			override fun decode(buffer: ByteBuf): V = decoder.decode(buffer)
			override fun encode(buffer: ByteBuf, value: V) = encoder.encode(value, buffer)
		}
		
		fun <V> unit(expectedValue: V): ByteBufCodec<V> = object : ByteBufCodec<V> {
			override fun decode(buffer: ByteBuf): V = expectedValue
			override fun encode(buffer: ByteBuf, value: V) {
				require(value == expectedValue) {
					"Can't encode '$value', expected '$expectedValue'"
				}
			}
		}
		
		fun <V> recursive(wrapper: UnaryOperator<ByteBufCodec<V>>): ByteBufCodec<V> {
			return object : ByteBufCodec<V> {
				private val resolvedCodec: Supplier<ByteBufCodec<V>> =
					Suppliers.memoize { wrapper.apply(this) }
				
				override fun decode(buffer: ByteBuf): V = resolvedCodec.get().decode(buffer)
				override fun encode(buffer: ByteBuf, value: V) = resolvedCodec.get().encode(buffer, value)
			}
		}
	}
	//endregion
	
	//region Transformations
	fun <O> map(
		decoderMapper: (V) -> O,
		encoderMapper: (O) -> V
	): ByteBufCodec<O> = object : ByteBufCodec<O> {
		override fun decode(buffer: ByteBuf): O = decoderMapper(this@ByteBufCodec.decode(buffer))
		override fun encode(buffer: ByteBuf, value: O) =
			this@ByteBufCodec.encode(buffer, encoderMapper(value))
	}
	
	fun <U> dispatch(
		typeGetter: (U) -> V,
		codecGetter: (V) -> ByteBufCodec<U>
	): ByteBufCodec<U> = object : ByteBufCodec<U> {
		override fun decode(buffer: ByteBuf): U {
			val type = this@ByteBufCodec.decode(buffer)
			return codecGetter(type).decode(buffer)
		}
		
		override fun encode(buffer: ByteBuf, value: U) {
			val type = typeGetter(value)
			this@ByteBufCodec.encode(buffer, type)
			codecGetter(type).encode(buffer, value)
		}
	}
	//endregion
}

//region Base interfaces
interface ByteBufDecoder<V> {
	fun decode(buffer: ByteBuf): V
}

interface ByteBufEncoder<V> {
	fun encode(buffer: ByteBuf, value: V)
}

interface ByteBufMemberEncoder<V> {
	fun encode(value: V, buffer: ByteBuf)
}
//endregion