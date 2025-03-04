package dev.kuchen.configuration.manager

import com.charleskorn.kaml.Yaml
import dev.kuchen.configuration.data.Config
import dev.kuchen.logger
import kotlin.io.path.Path

class ConfigManager {
    private val currentDirectory = System.getProperty("user.dir")
    private val configFile = "config.yml"

    fun create() {
        val content = ConfigManager::class.java.getResourceAsStream(configFile)

        val file = Path("$currentDirectory$configFile").toFile()
        if (!file.exists()) {
            file.createNewFile()
            logger.info("Created configuration file at location: $currentDirectory/$configFile")

            if (content != null) {
                file.appendBytes(content.readAllBytes())
                logger.info("Wrote all contents to configuration file")
            }
        }
    }

    fun query(): Config {
        val file = Path("$currentDirectory$configFile").toFile()

        return Yaml.default.decodeFromString(Config.serializer(), file.readText())
    }
}