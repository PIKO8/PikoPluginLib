package ru.piko.pikopluginlib.Functions.Builder

data class RepeatData(
	var count: Int = 0,
	val maxRepeats: Int,
	val resultTrue: BuilderResult,
	val resultFalse: BuilderResult
)