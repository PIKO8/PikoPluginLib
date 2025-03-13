// Плагины для сборки проекта
plugins {
	`java-library`
	java                                               // Базовая поддержка Java
	`maven-publish`                                    // Публикация в Maven репозиторий
	id("com.github.johnrengelman.shadow") version "7.1.2"  // Сборка JAR с зависимостями
	kotlin("jvm") version "2.1.0"                      // Поддержка Kotlin
	id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}


// Основные параметры проекта
group = "ru.piko"
version = "0.1.17-beta.4"
val minecraftVersion = "1.21.4"
val kotlinVersion = "2.1.0"
val targetJavaVersion by extra(21)

// Группы библиотек для разных сборок
val libraryGroups = mapOf<String, List<String>>(
	"Main" to listOf(),                                // Основной JAR без доп. библиотек
//	"KotlinMin" to listOf("kotlin-stdlib"),            // Минимальная версия с Kotlin
//	"KotlinMax" to listOf("kotlin-stdlib", "kotlin-reflect") // Максимальная версия с Kotlin и рефлексией
)

// Репозитории для загрузки зависимостей
repositories {
	mavenCentral()                                     // Центральный Maven репозиторий
	maven("https://repo.papermc.io/repository/maven-public/") {
		name = "PaperMC"
	}
	maven("https://oss.sonatype.org/content/groups/public/") {
		name = "sonatype"
	}
	maven("https://repo.codemc.io/repository/maven-public/") {
		name = "CodeMC"
	}
}

// Зависимости проекта
dependencies {
	paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
	
	implementation("de.tr7zw:item-nbt-api-plugin:2.14.1")
	implementation("org.jetbrains:annotations:24.0.1")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
}

afterEvaluate {
	// Динамическая генерация JAR для каждой группы библиотек
	libraryGroups.forEach { (groupName, libraries) ->
		tasks.create("jarWith${groupName}Group", Jar::class) {
			from(sourceSets.main.get().output)
			
			// Для Main группы не добавляем никаких дополнительных библиотек
			if (libraries.isNotEmpty()) {
				from({
					configurations.runtimeClasspath.get()
						.filter { jar -> libraries.any { lib -> jar.name.contains(lib) } }
						.map { if (it.isDirectory) it else zipTree(it) }
				})
			}
			
			archiveFileName.set("PikoPluginLib-${groupName}-${minecraftVersion}-${project.version}.jar")
			duplicatesStrategy = DuplicatesStrategy.EXCLUDE
			
			// Явно исключаем дублирующиеся файлы
			exclude("META-INF/*.SF")
			exclude("META-INF/*.DSA")
			exclude("META-INF/*.RSA")
			exclude("META-INF/PikoPluginLib.kotlin_module")
			
			doFirst {
				// Дополнительная фильтрация дубликатов
				duplicatesStrategy = DuplicatesStrategy.EXCLUDE
			}
		}
		tasks.named("build") {
			dependsOn("jarWith${groupName}Group")
		}
	}
	
	// Конфигурация публикации артефактов
	publishing {
		publications {
			create<MavenPublication>("mavenJava") {
				artifact(tasks.getByName("jarWithMainGroup").outputs.files.singleFile)
				groupId = "ru.piko"
				artifactId = "pikopluginlib"
			}
		}
		
		// Репозиторий для загрузки пакетов на GitHub
		repositories {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/PIKO8/PikoPluginLib")
				credentials {
					username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
					password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
				}
			}
		}
	}
	// Публикация только после того как соберётся jar
	tasks.named("publishMavenJavaPublicationToMavenLocal") {
		dependsOn(tasks.named("jarWithMainGroup"))
	}
}

// Конфигурация Java
java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

// Создаем таск для сборки всех групп JAR
tasks.register("buildAllGroups") {
	dependsOn(tasks.matching { task ->
		task.name.startsWith("jarWith") && task.name.endsWith("Group")
	})
}

// Таск для сборки конкретной группы библиотек (для тестирования)
tasks.register("buildSpecificGroup") {
	doLast {
		val selectedGroup = project.findProperty("libraryGroup") as String? ?: "Main"
		tasks.getByName("jarWith${selectedGroup}Group").actions.forEach {
			it.execute(tasks.getByName("jarWith${selectedGroup}Group"))
		}
	}
}

// Настройки компиляции Java
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release.set(targetJavaVersion)
}

// Настройки компиляции Kotlin
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = targetJavaVersion.toString()
	}
}

// Обработка ресурсов с заменой переменных
tasks.processResources {
	val props = mapOf("version" to version)
	inputs.properties(props)
	filteringCharset = "UTF-8"
	filesMatching("plugin.yml") {
		expand(props)
	}
}

// Настройка Kotlin toolchain
kotlin {
	jvmToolchain(targetJavaVersion)
}

// Добавление артефактов в сборку
artifacts {
	tasks.matching { task ->
		task.name.startsWith("jarWith") && task.name.endsWith("Group")
	}.forEach { archives(it) }
}

// Утилитный таск для вывода версии
tasks.register("printVersion") {
	doLast {
		println("$minecraftVersion-$version")
	}
}

// При сборке проекта будут создаваться все группы
tasks.build {
	dependsOn("buildAllGroups")
}

// Отключаем стандартный JAR
tasks.jar {
	enabled = false
}
