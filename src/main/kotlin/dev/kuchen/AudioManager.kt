import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.audio.AudioSendHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class AudioPlayerManager {
    private val playerManager = DefaultAudioPlayerManager()
    private val trackQueue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()
    val player = playerManager.createPlayer()
    var loop = false

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
        AudioSourceManagers.registerLocalSource(playerManager)

        player.addListener { event ->
            if (event is com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent) {
                if (loop) {
                    player.startTrack(event.track.makeClone(), true)
                } else if (event.endReason.mayStartNext) {
                    nextTrack()
                }
            }
        }
    }

    fun loadAndPlay(audioPath: String) {
        playerManager.loadItem(audioPath, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                if (!player.startTrack(track, true)) {
                    trackQueue.offer(track)
                }
            }

            override fun playlistLoaded(playlist: com.sedmelluq.discord.lavaplayer.track.AudioPlaylist) {
                for (track in playlist.tracks) {
                    trackQueue.offer(track)
                }
                if (player.playingTrack == null) {
                    nextTrack()
                }
            }

            override fun noMatches() {}
            override fun loadFailed(exception: FriendlyException) {
                println("Laden des Tracks fehlgeschlagen: ${exception.message}")
            }
        })
    }

    fun nextTrack() {
        val next = trackQueue.poll()
        if (next != null) {
            player.startTrack(next, false)
        }
    }

    fun toggleLoop() {
        loop = !loop
        println("Loop-Modus ist jetzt: ${if (loop) "Aktiviert" else "Deaktiviert"}")
    }

    fun getSendHandler(): AudioSendHandler {
        return AudioPlayerSendHandler(player)
    }
}
