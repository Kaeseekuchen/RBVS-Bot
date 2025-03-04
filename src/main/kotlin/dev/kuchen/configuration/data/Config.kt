package dev.kuchen.configuration.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token: String,
    @SerialName("support_channel_id")
    val supportChannelId: String,
    @SerialName("audio_file_name")
    val audioFileName: String
)
