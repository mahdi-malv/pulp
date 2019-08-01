package ir.malv.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter


/**
 * If you want to be able to config the pulp from manifest, you need to initialize it first.
 * In your application class call `Pulp.init(this)`
 *
 */
object Pulp {

    private var LOG_TAG = "Pulp"
    private var handlers: MutableList<LogHandler> = mutableListOf()
    private var logEnabled = true
    private var handlersEnabled = true

    /**
     * If you have added manifest config for logger,
     * you must call `Pulp.init(context)` to make that work.
     * Pulp needs context to interact with manifest.
     */
    fun init(context: Context): Pulp {
        val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData.get("pulp_enabled") ?: return this
        logEnabled = if (listOf("true", "1", "yes", "ok").contains(value.toString().toLowerCase())) {
            true
        } else !listOf("false", "0", "no", "nope").contains(value.toString())
        return this
    }

    /**
     * When logging, Pulp adds a tag, with this Pulp will replace the tag.
     * @param tag will be the used tag
     */
    fun setMainTag(tag: String): Pulp {
        LOG_TAG = tag
        return this
    }

    /**
     * Every log created to be shown will also be sent to all handlers.
     * Using this method you can add handler to list of handlers.
     */
    fun addHandler(logHandler: LogHandler): Pulp {
        handlers.add(logHandler)
        return this
    }

    /**
     * Calling this with false param, will cause the handlers, not to be nitified.
     * This is separate from log enabled. Even if the log is disabled, handlers will be notified.
     */
    fun setHandlerEnabled(enabled: Boolean): Pulp {
        handlersEnabled = enabled
        return this
    }

    fun debug(tags: Array<String>, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.D, tags.toList(), message, error, mapData)
    }

    fun debug(tag: String, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) =
        debug(arrayOf(tag), message, error, data)

    fun info(tags: Array<String>, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.I, tags.toList(), message, error, mapData)
    }

    fun info(tag: String, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) =
        info(arrayOf(tag), message, error, data)

    fun warn(tags: Array<String>, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.W, tags.toList(), message, error, mapData)
    }

    fun warn(tag: String, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) =
        warn(arrayOf(tag), message, error, data)

    fun error(tags: Array<String>, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.E, tags.toList(), message, error, mapData)
    }

    fun error(tag: String, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) =
        error(arrayOf(tag), message, error, data)

    fun wtf(tags: Array<String>, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.WTF, tags.toList(), message, error, mapData)
    }

    fun wtf(tag: String, message: String, error: Throwable? = null, data: LogData.() -> Unit = {}) =
        wtf(arrayOf(tag), message, error, data)

    private fun log(level: Level, tags: List<String>, message: String, t: Throwable? = null, data: LogData) {
        if (handlersEnabled) {
            for (h in handlers) {
                h.onLog(level, tags, message, t, data)
            }
        }
        if (!logEnabled) return
        val logMessage = logMessage(tags, message, t, data)
        when (level) {
            Pulp.Level.I -> if (t != null) Log.i(LOG_TAG, logMessage, t) else Log.i(
                LOG_TAG, logMessage)
            Pulp.Level.D -> if (t != null) Log.d(LOG_TAG, logMessage, t) else Log.d(
                LOG_TAG, logMessage)
            Pulp.Level.W -> if (t != null) Log.w(LOG_TAG, logMessage, t) else Log.w(
                LOG_TAG, logMessage)
            Pulp.Level.E,
            Pulp.Level.WTF -> if (t != null) Log.e(LOG_TAG, logMessage, t) else Log.e(
                LOG_TAG, logMessage)
        }
    }

    private fun logMessage(tags: List<String>, message: String, t: Throwable?, logData: LogData): String {
        val writer = StringWriter()
        t?.printStackTrace(PrintWriter(writer))
        val stackTrace = writer.toString()
        return """
$LOG_TAG:
Tags: $tags
Message: $message
${if (logData.data.isNotEmpty()) "Data:\n${logData.data.map { "\t${it.key}\t${it.value}" }.joinToString("\n")}" else ""}
${if (!stackTrace.isBlank()) "StackTrace:\n$stackTrace" else ""}
              """.trimIndent()
        }


    /**
     * Sout will not work as a regular log. It will not notify handlers and is not using Pulp format. It's just a sysout.
     */
    fun sout(message: String) = println("$LOG_TAG ### $message")

    enum class Level {
        D, I, W, E, WTF
    }

    interface LogHandler {
        fun onLog(level: Level, tags: List<String>, message: String, t: Throwable? = null, data: LogData)
    }

    class LogData {
        val data = HashMap<String, String>()
        infix fun String.to(value: String) {
            data[this] = value
        }
    }

}
