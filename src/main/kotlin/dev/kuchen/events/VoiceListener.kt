package dev.kuchen.events

import AudioPlayerManager
import dev.kuchen.configuration.data.Config
import dev.minn.jda.ktx.events.listener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.managers.AudioManager
import org.slf4j.LoggerFactory

class VoiceListener(val api: JDA, val config: Config) {
    private val logger = LoggerFactory.getLogger(VoiceListener::class.java)
    private val audioPlayerManager = AudioPlayerManager()

    init {
        api.listener<GuildVoiceUpdateEvent> { event ->
            val guild = event.guild
            val audioManager = guild.audioManager

            if (event.channelJoined?.id == config.supportChannelId) {
                if (!audioManager.isConnected) connectAudioManager(audioManager, api.getVoiceChannelById(config.supportChannelId)!!)

                audioPlayerManager.loadAndPlay("${System.getProperty("user.dir")}/${config.audioFileName}")
            }

            if (event.channelLeft?.id == config.supportChannelId) {
                if (!audioManager.isConnected) connectAudioManager(audioManager, api.getVoiceChannelById(config.supportChannelId)!!)

                val channel = event.channelLeft as VoiceChannel
                val remainingUsers = channel.members.filter { it.user.id != api.selfUser.id }

                if (remainingUsers.isEmpty()) {
                    audioManager.closeAudioConnection()
                    logger.info("Left channel")
                }
            }
        }
    }

    private fun connectAudioManager(audioManager: AudioManager, channel: VoiceChannel) {
        audioManager.openAudioConnection(channel)
        audioManager.sendingHandler = audioPlayerManager.getSendHandler()

        logger.info("Joined channel")
    }
}