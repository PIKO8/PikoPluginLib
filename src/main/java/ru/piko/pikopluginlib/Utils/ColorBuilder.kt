package ru.piko.pikopluginlib.Utils

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.bossbar.BossBar.Color as BossBarColor
import org.bukkit.Color as BukkitColor
import java.awt.Color as JavaColor

class ColorBuilder private constructor(
	val red: Int,
	val green: Int,
	val blue: Int,
	val alpha: Int = 255
) {
	// Конвертация в TextColor
	@get:JvmName("toTextColor")
	val textColor: TextColor get() = TextColor.color(red, green, blue)
	
	// Конвертация в BossBar Color
	@get:JvmName("toBossBarColor")
	val bossBarColor: BossBarColor get() = when {
		red > 200 && green < 100 && blue < 100 -> BossBarColor.RED
		red < 100 && green > 200 && blue < 100 -> BossBarColor.GREEN
		red < 100 && green < 100 && blue > 200 -> BossBarColor.BLUE
		red > 200 && green > 200 && blue < 100 -> BossBarColor.YELLOW
		red > 200 && blue > 200 && green < 100 -> BossBarColor.PINK
		red > 100 && green < 100 && blue > 100 -> BossBarColor.PURPLE
		red > 200 && green > 200 && blue > 200 -> BossBarColor.WHITE
		else -> BossBarColor.PURPLE // По умолчанию
	}
	
	// Конвертация в Bukkit Color
	@get:JvmName("toBukkitColor")
	val bukkitColor: BukkitColor get() = BukkitColor.fromRGB(red, green, blue)
	
	// Конвертация в AWT Color
	@get:JvmName("toJavaColor")
	val javaColor: JavaColor get() = JavaColor(red, green, blue, alpha)
	
	@get:JvmName("hexString")
	val hexString: String get() = toHexString()
	
	fun toHexString(
		format: HexFormat = HexFormat.RRGGBB,
		prefix: String = "#"
	): String {
		val r = red.toString(16).padStart(2, '0')
		val g = green.toString(16).padStart(2, '0')
		val b = blue.toString(16).padStart(2, '0')
		val a = alpha.toString(16).padStart(2, '0')
		
		return prefix + when (format) {
			HexFormat.RGB -> "${r[0]}${g[0]}${b[0]}"
			HexFormat.RGBA -> "${r[0]}${g[0]}${b[0]}${a[0]}"
			HexFormat.RRGGBB -> "$r$g$b"
			HexFormat.RRGGBBAA -> "$r$g$b$a"
		}
	}
	
	enum class HexFormat {
		RGB,        // #RGB (3 символа)
		RGBA,       // #RGBA (4 символа)
		RRGGBB,     // #RRGGBB (6 символов)
		RRGGBBAA    // #RRGGBBAA (8 символов)
	}
	
	companion object {
		
		val JavaColor.builder: ColorBuilder get() = ColorBuilder(this.red, this.green, this.blue, this.alpha)
		val BukkitColor.builder: ColorBuilder get() = ColorBuilder(this.red, this.green, this.blue, this.alpha)
		val TextColor.builder: ColorBuilder get() = ColorBuilder(this.red(), this.green(), this.blue())
		
		
		// Создание из RGB
		fun fromRGB(red: Int, green: Int, blue: Int, alpha: Int = 255): ColorBuilder {
			require(red in 0..255 && green in 0..255 && blue in 0..255 && alpha in 0..255) {
				"RGB values must be between 0 and 255"
			}
			return ColorBuilder(red, green, blue, alpha)
		}
		
		// Создание из HEX
		fun fromHex(hex: String, alpha: Int = 255): ColorBuilder {
			val cleanHex = hex
				.removePrefix("0x")
				.removePrefix("0X")
				.removePrefix("#")
				.removePrefix("\\u")
				.removePrefix("U+")
			
			return when (cleanHex.length) {
				3 -> { // #rgb формат
					val red = cleanHex[0].toString().repeat(2).toInt(16)
					val green = cleanHex[1].toString().repeat(2).toInt(16)
					val blue = cleanHex[2].toString().repeat(2).toInt(16)
					fromRGB(red, green, blue, alpha)
				}
				4 -> { // #rgba формат
					val red = cleanHex[0].toString().repeat(2).toInt(16)
					val green = cleanHex[1].toString().repeat(2).toInt(16)
					val blue = cleanHex[2].toString().repeat(2).toInt(16)
					val newAlpha = cleanHex[3].toString().repeat(2).toInt(16)
					fromRGB(red, green, blue, newAlpha)
				}
				6 -> { // #rrggbb формат
					val red = cleanHex.substring(0, 2).toInt(16)
					val green = cleanHex.substring(2, 4).toInt(16)
					val blue = cleanHex.substring(4, 6).toInt(16)
					fromRGB(red, green, blue, alpha)
				}
				8 -> { // #rrggbbaa формат
					val red = cleanHex.substring(0, 2).toInt(16)
					val green = cleanHex.substring(2, 4).toInt(16)
					val blue = cleanHex.substring(4, 6).toInt(16)
					val newAlpha = cleanHex.substring(6, 8).toInt(16)
					fromRGB(red, green, blue, newAlpha)
				}
				else -> throw IllegalArgumentException("Invalid hex color format")
			}
		}
		
		// Создание из HSV
		fun fromHSV(hue: Float, saturation: Float, value: Float, alpha: Int = 255): ColorBuilder {
			require(hue in 0f..360f && saturation in 0f..1f && value in 0f..1f) {
				"HSV values out of range"
			}
			
			val c = value * saturation
			val x = c * (1 - kotlin.math.abs((hue / 60) % 2 - 1))
			val m = value - c
			
			val (r, g, b) = when {
				hue < 60 -> Triple(c, x, 0f)
				hue < 120 -> Triple(x, c, 0f)
				hue < 180 -> Triple(0f, c, x)
				hue < 240 -> Triple(0f, x, c)
				hue < 300 -> Triple(x, 0f, c)
				else -> Triple(c, 0f, x)
			}
			
			val red = ((r + m) * 255).toInt()
			val green = ((g + m) * 255).toInt()
			val blue = ((b + m) * 255).toInt()
			
			return fromRGB(red, green, blue, alpha)
		}
		
		// Создание из процентного RGB (0.0 - 1.0)
		fun fromPercentRGB(
			redPercent: Float,
			greenPercent: Float,
			bluePercent: Float,
			alphaPercent: Float = 1f
		): ColorBuilder {
			require(redPercent in 0f..1f &&
					greenPercent in 0f..1f &&
					bluePercent in 0f..1f &&
					alphaPercent in 0f..1f) {
				"Percent values must be between 0.0 and 1.0"
			}
			
			val red = (redPercent * 255).toInt()
			val green = (greenPercent * 255).toInt()
			val blue = (bluePercent * 255).toInt()
			val alpha = (alphaPercent * 255).toInt()
			
			return fromRGB(red, green, blue, alpha)
		}
		
		// Предопределенные цвета
		val RED = fromRGB(255, 0, 0)
		val GREEN = fromRGB(0, 255, 0)
		val BLUE = fromRGB(0, 0, 255)
		val WHITE = fromRGB(255, 255, 255)
		val BLACK = fromRGB(0, 0, 0)
	}
}