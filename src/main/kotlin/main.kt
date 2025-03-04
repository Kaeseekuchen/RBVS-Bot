import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import javax.security.auth.login.LoginException
import java.io.File
import java.io.InputStream

fun main() {
    val token = ""

    try {
        val jda = JDABuilder.createDefault(token)
            .addEventListeners(VoiceListener())
            .build()

        jda.awaitReady()
        println("Bot online")
    } catch (e: LoginException) {
        e.printStackTrace()
    }
}


class VoiceListener : ListenerAdapter() {
    private val supportChannelId = ""
    private val audioPlayerManager = AudioPlayerManager()

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val bot = event.jda.selfUser
        val guild = event.guild
        val audioManager = guild.audioManager

        if (event.channelJoined?.id == supportChannelId) {
            if (!audioManager.isConnected) {
                val channel = event.channelJoined as VoiceChannel
                audioManager.openAudioConnection(channel)
                audioManager.sendingHandler = audioPlayerManager.getSendHandler()
                println("Bot has joined the channel")

                audioPlayerManager.loadAndPlay("src/main/resources/old/audio.mp3")
            }
        }

        if (event.channelLeft?.id == supportChannelId) {
            val channel = event.channelLeft as VoiceChannel
            val remainingUsers = channel.members.filter { it.user.id != bot.id }

            if (remainingUsers.isEmpty() && audioManager.isConnected) {
                audioManager.closeAudioConnection()
                println("Bot has left the channel")
            }
        }
    }
}