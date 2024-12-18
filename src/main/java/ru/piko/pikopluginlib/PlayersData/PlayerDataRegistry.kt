package ru.piko.pikopluginlib.PlayersData

class PlayerDataRegistry {
	val function: (PlayerData) -> APlayerData
	val data: Map<String, Any> // TODO? А в функцию почему не передаётся?
	val load: Boolean
	val unload: Boolean
	
	constructor(load: Boolean, unload: Boolean, function: (PlayerData) -> APlayerData) {
		this.data = emptyMap()
		this.load = load
		this.unload = unload
		this.function = function
	}
	
	constructor(data: Map<String, Any>, function: (PlayerData) -> APlayerData) {
		this.data = data
		this.load = data["load"] as? Boolean ?: false
		this.unload = data["unload"] as? Boolean ?: false
		this.function = function
	}
	
}