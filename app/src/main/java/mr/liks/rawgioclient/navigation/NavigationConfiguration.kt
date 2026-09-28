package mr.liks.rawgioclient.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import mr.liks.core.navigation.TopLevelRoute
import mr.liks.feature.detais.api.GameDetailsRoute
import mr.liks.feature.feed.api.FeedRoute
import mr.liks.feature.settings.api.SettingsRoute

/** Настройки хранения навигации */
val NavigationConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(FeedRoute::class, FeedRoute.serializer())
            subclass(GameDetailsRoute::class, GameDetailsRoute.serializer())
            subclass(SettingsRoute::class, SettingsRoute.serializer())
            subclass(TopLevelRoute.Feed::class, TopLevelRoute.Feed.serializer())
            subclass(TopLevelRoute.Search::class, TopLevelRoute.Search.serializer())
            subclass(TopLevelRoute.Settings::class, TopLevelRoute.Settings.serializer())
        }
    }
}