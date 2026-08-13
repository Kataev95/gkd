package li.songe.gkd.util

import androidx.compose.ui.graphics.vector.ImageVector
import li.songe.gkd.ui.component.PerfIcon

sealed interface Option<T> {
    val value: T
    val label: String
    val options: List<Option<T>>
}

sealed interface OptionIcon {
    val icon: ImageVector
}

sealed interface OptionMenuLabel {
    val menuLabel: String
}

fun <V, T : Option<V>> Iterable<T>.findOption(value: V): T {
    return find { it.value == value } ?: first()
}

sealed class AppSortOption(override val value: Int, override val label: String) : Option<Int> {
    override val options get() = objects

    data object ByAppName : AppSortOption(0, "По названию")
    data object ByActionTime : AppSortOption(2, "По последнему срабатыванию")
    data object ByUsedTime : AppSortOption(3, "По последнему использованию")

    companion object {
        val objects by lazy { listOf(ByAppName, ByUsedTime, ByActionTime) }
    }
}

sealed class UpdateTimeOption(
    override val value: Long,
    override val label: String
) : Option<Long> {
    override val options get() = objects

    data object Pause : UpdateTimeOption(-1, "Не обновлять")
    data object Everyday : UpdateTimeOption(24 * 60 * 60_000, "Каждый день")
    data object Every3Days : UpdateTimeOption(24 * 60 * 60_000 * 3, "Каждые 3 дня")
    data object Every7Days : UpdateTimeOption(24 * 60 * 60_000 * 7, "Каждые 7 дней")

    companion object {
        val objects by lazy { listOf(Pause, Everyday, Every3Days, Every7Days) }
    }
}

sealed class DarkThemeOption(
    override val value: Boolean?,
    override val label: String,
    override val menuLabel: String,
    override val icon: ImageVector
) : Option<Boolean?>, OptionIcon, OptionMenuLabel {
    override val options get() = objects

    data object FollowSystem : DarkThemeOption(null, "Системная", "Системная", PerfIcon.AutoMode)
    data object AlwaysEnable : DarkThemeOption(true, "Включена", "Тёмная", PerfIcon.DarkMode)
    data object AlwaysDisable : DarkThemeOption(false, "Выключена", "Светлая", PerfIcon.LightMode)

    companion object {
        val objects by lazy { listOf(FollowSystem, AlwaysEnable, AlwaysDisable) }
    }
}

sealed class EnableGroupOption(
    override val value: Boolean?,
    override val label: String
) : Option<Boolean?> {
    override val options get() = objects

    data object FollowSubs : EnableGroupOption(null, "Как в подписке")
    data object AllEnable : EnableGroupOption(true, "Включить все")
    data object AllDisable : EnableGroupOption(false, "Отключить все")

    companion object {
        val objects by lazy { listOf(FollowSubs, AllEnable, AllDisable) }
    }
}

sealed class RuleSortOption(override val value: Int, override val label: String) : Option<Int> {
    override val options get() = objects

    data object ByDefault : RuleSortOption(0, "По умолчанию")
    data object ByActionTime : RuleSortOption(1, "По последнему срабатыванию")
    data object ByRuleName : RuleSortOption(2, "По названию правила")

    companion object {
        val objects by lazy { listOf(ByDefault, ByActionTime, ByRuleName) }
    }
}

sealed class UpdateChannelOption(
    override val value: Int,
    override val label: String,
    val url: String
) : Option<Int> {
    override val options get() = objects

    data object Stable : UpdateChannelOption(
        0,
        "Стабильная",
        "https://registry.npmmirror.com/@gkd-kit/app/latest/files/index.json"
    )

    data object Beta : UpdateChannelOption(
        1,
        "Бета",
        "https://registry.npmmirror.com/@gkd-kit/app-beta/latest/files/index.json"
    )

    companion object {
        val objects by lazy { listOf(Stable, Beta) }
    }
}

sealed interface BinaryOption : Option<Int> {
    fun include(flag: Int): Boolean = (value and flag) != 0
    fun invert(flag: Int): Int = value xor flag

    companion object {
        fun combine(options: Collection<BinaryOption>): Int {
            return options.fold(0) { a, b -> a or b.value }
        }
    }
}


sealed class AppGroupOption(
    override val value: Int,
    override val label: String
) : BinaryOption {
    override val options get() = allObjects

    data object SystemGroup : AppGroupOption(1 shl 0, "Системные приложения")
    data object UserGroup : AppGroupOption(1 shl 1, "Пользовательские приложения")
    data object UnInstalledGroup : AppGroupOption(1 shl 2, "Неустановленные приложения")

    companion object {
        val normalObjects by lazy { listOf(SystemGroup, UserGroup) }
        val allObjects by lazy { listOf(SystemGroup, UserGroup, UnInstalledGroup) }
    }
}

sealed class AutomatorModeOption(
    override val value: Int,
    override val label: String,
) : Option<Int> {
    override val options get() = objects

    data object A11yMode : AutomatorModeOption(1, "Спец. возможности")
    data object AutomationMode : AutomatorModeOption(2, "Автоматизация")

    companion object {
        val objects by lazy { listOf(A11yMode, AutomationMode) }
    }
}
