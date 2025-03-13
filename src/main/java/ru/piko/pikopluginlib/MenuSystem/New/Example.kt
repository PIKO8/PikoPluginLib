package ru.piko.pikopluginlib.MenuSystem.New

import org.bukkit.Material
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ComponentExtend.mini
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ItemStackExtend
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ItemStackExtend.item
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ItemStackExtend.metaItem
import java.awt.SystemColor.menu

//fun main() {
//
//	val exampleMenu = menu("Пример меню", 3) {
//
//		slot(2, 5, metaItem(Material.DIAMOND) { displayName(mini("<aqua>Алмаз!")) }) { // line, slot, item
//			onClick {
//				player.addSafeItem(item(Material.DIAMOND))
//			}
//			onRender {
//				showItem
//			}
//			onUpdate(20) { // ticks
//				showItem = showItem?.amount?.inc()
//			}
//		}
//	}
//
//
//}
