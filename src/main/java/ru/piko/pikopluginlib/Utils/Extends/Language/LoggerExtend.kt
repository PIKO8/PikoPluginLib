package ru.piko.pikopluginlib.Utils.Extends.Language

import java.util.logging.Level
import java.util.logging.Logger

object LoggerExtend {
	
	inline fun Logger.infoInline(block: () -> String) {
		log(Level.INFO, block())
	}
	
}