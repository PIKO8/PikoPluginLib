package ru.piko.pikopluginlib.PlayersData

import io.netty.buffer.ByteBuf

class ByteBufPlayerData(
	override val id : String,
	val buffer: ByteBuf
) : ADynamicPlayerData()