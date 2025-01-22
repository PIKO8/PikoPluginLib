pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://repo.papermc.io/repository/maven-public/")
	}
	plugins {
		kotlin("jvm") version "2.1.0"
	}
	resolutionStrategy {
		eachPlugin {
			if (requested.id.id == "org.jetbrains.kotlin.jvm") {
				useVersion("2.1.0")
			}
		}
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "PikoPluginLib"