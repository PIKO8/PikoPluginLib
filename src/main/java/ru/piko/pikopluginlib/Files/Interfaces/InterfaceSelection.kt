package ru.piko.pikopluginlib.Files.Interfaces

import ru.piko.pikopluginlib.Files.Abstract.selection.AbstractSelection

interface InterfaceSelection<S: AbstractSelection> {
	
	fun addResource(resource: S): Boolean
	fun removeResource(resource: S): Boolean
}