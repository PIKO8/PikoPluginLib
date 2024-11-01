package ru.piko.pikopluginlib.Functions.Builder

data class CounterData(
	var current: Int = 0,
	val every: Int,
	val resultTrue: BuilderResult,
	val resultFalse: BuilderResult
)