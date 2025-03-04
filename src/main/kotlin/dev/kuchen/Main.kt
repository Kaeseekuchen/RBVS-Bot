package dev.kuchen

import AudioPlayerManager
import dev.kuchen.configuration.data.Config
import dev.kuchen.configuration.manager.ConfigManager
import dev.kuchen.events.VoiceListener
import dev.minn.jda.ktx.jdabuilder.light
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("dev.kuchen.Main")

fun main() {
    val configManager = ConfigManager()
    configManager.create()
    val config = configManager.query()

    val token = config.token

    val api = light(token, enableCoroutines = true)

    api.addEventListener(VoiceListener(api, config))

    logger.info("Bot online")
}