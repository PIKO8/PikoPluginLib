package ru.piko.pikopluginlib.PlayersData

import ru.piko.pikopluginlib.Utils.ByteBufCodec

interface IPlayerDataRegistry {
	fun create(): APlayerData
	
	val id: String
	val load: Boolean
	val unload: Boolean
}

class CommonPlayerDataRegistry(
	override val id: String,
	override val load: Boolean,
	override val unload: Boolean,
	val function: () -> APlayerData,
) : IPlayerDataRegistry {
	
	override fun create(): APlayerData {
		return function()
	}
	
}

class CodecPlayerDataRegistry<V : APlayerData>(
	override val id: String,
	override val load: Boolean,
	override val unload: Boolean,
	val codec: ByteBufCodec<V>,
	val default: () -> V
) : IPlayerDataRegistry {
	
	override fun create(): APlayerData {
		return default()
	}
}
