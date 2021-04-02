package ir.malv.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import ir.malv.utils.db.PulpDatabaseImpl
import ir.malv.utils.db.PulpItem
import java.util.*
import kotlin.collections.HashMap


/**
 * If you want to be able to config the pulp from manifest, you need to initialize it first.
 * In your application class call `Pulp.init(this)`
 *
 */
object Pulp {

    private var LOG_TAG = ""
    private var handlers: MutableList<LogHandler> = mutableListOf()
    private var logEnabled = true
    private var databaseEnabled = false
    private var handlersEnabled = true

    private var applicationContext: Context? = null

    /**
     * If you have added manifest config for logger,
     * you must call `Pulp.init(context)` to make that work.
     * Pulp needs context to interact with manifest.
     */
    @JvmStatic
    fun init(context: Context): Pulp {
        applicationContext = context.applicationContext
        val ai = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val value = ai.metaData?.get("pulp_enabled") ?: return this
        logEnabled =
            if (listOf("true", "1", "yes", "ok").contains(value.toString().toLowerCase(Locale.ROOT))) {
                true
            } else !listOf("false", "0", "no", "nope").contains(value.toString())
        return this
    }

    @JvmStatic
    fun setApplicationContext(context: Context): Pulp {
        applicationContext = context.applicationContext
        return this
    }

    /**
     * When logging, Pulp adds a tag, with this Pulp will replace the tag.
     * @param tag will be the used tag
     */
    @JvmStatic
    @Deprecated("MainTag is now useless. Tags of logs will be used instead")
    fun setMainTag(tag: String): Pulp {
        LOG_TAG = tag
        return this
    }

    @JvmStatic
    fun setLogsEnabled(enabled: Boolean): Pulp {
        logEnabled = enabled
        return this
    }

    @JvmStatic
    fun setDatabaseEnabled(enabled: Boolean): Pulp {
        databaseEnabled = enabled
        return this
    }

    /**
     * In order to interact with logs saved into database, this function will return a LiveData of all items.
     * @param context is needed for database, but if [Pulp.init] was called, it's not needed to enter context.
     *
     * If [Pulp.init] was not called and
     */
    @JvmStatic
    fun getSavedLogs(context: Context): LiveData<List<PulpItem>> {
        return PulpDatabaseImpl.savedLogs(context)
    }

    @JvmStatic
    fun clearLogs(context: Context) {
        PulpDatabaseImpl.clearLogs(context)
    }

    /**
     * Every log created to be shown will also be sent to all handlers.
     * Using this method you can add handler to list of handlers.
     */
    @JvmStatic
    fun addHandler(logHandler: LogHandler): Pulp {
        handlers.add(logHandler)
        return this
    }

    @JvmStatic
    fun removeAllHandlers() = handlers.clear()

    /**
     * Calling this with false param, will cause the handlers, not to be nitified.
     * This is separate from log enabled. Even if the log is disabled, handlers will be notified.
     */
    @JvmStatic
    fun setHandlerEnabled(enabled: Boolean): Pulp {
        handlersEnabled = enabled
        return this
    }

    // ------ Logs

    @JvmStatic
    @JvmOverloads
    fun debug(
        tags: Array<String>,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.D, tags.toList(), message.toString(), error, mapData)
    }

    @JvmStatic
    @JvmOverloads
    fun debug(
        tag: String,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) =
        debug(arrayOf(tag), message, error, data)

    @JvmStatic
    @JvmOverloads
    fun info(
        tags: Array<String>,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.I, tags.toList(), message.toString(), error, mapData)
    }

    @JvmStatic
    @JvmOverloads
    fun info(
        tag: String,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) =
        info(arrayOf(tag), message, error, data)

    @JvmStatic
    @JvmOverloads
    fun warn(
        tags: Array<String>,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.W, tags.toList(), message.toString(), error, mapData)
    }

    @JvmStatic
    @JvmOverloads
    fun warn(
        tag: String,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) =
        warn(arrayOf(tag), message, error, data)

    @JvmStatic
    @JvmOverloads
    fun error(
        tags: Array<String>,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.E, tags.toList(), message.toString(), error, mapData)
    }

    @JvmStatic
    @JvmOverloads
    fun error(
        tag: String,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) =
        error(arrayOf(tag), message, error, data)

    @JvmStatic
    @JvmOverloads
    fun wtf(
        tags: Array<String>,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) {
        val mapData = LogData()
        mapData.data()
        log(Pulp.Level.WTF, tags.toList(), message.toString(), error, mapData)
    }

    @JvmStatic
    @JvmOverloads
    fun wtf(
        tag: String,
        message: String?,
        error: Throwable? = null,
        data: LogData.() -> Unit = {}
    ) =
        wtf(arrayOf(tag), message, error, data)

    private fun log(
        level: Level,
        tags: List<String>,
        message: String,
        t: Throwable? = null,
        data: LogData
    ) {

        if (!logEnabled) return

        val time = System.currentTimeMillis()

        if (handlersEnabled) {
            for (h in handlers) {
                h.onLog(level, tags, message, t, data, time)
            }
        }

        if (databaseEnabled) {
            applicationContext?.let {
                PulpDatabaseImpl.insert(it, level, tags, message, t, data, time)
            }
        }
    }

    /**
     * Sout will not work as a regular log. It will not notify handlers and is not using Pulp format. It's just a sysout.
     */
    @JvmStatic
    @Deprecated("Use other logs and instead use a different LogHandler other than LogCatListener")
    fun sout(message: String) = println("$LOG_TAG ### $message")

    enum class Level {
        D, I, W, E, WTF
    }

    interface LogHandler {
        fun onLog(
            level: Level,
            tags: List<String>,
            message: String,
            t: Throwable? = null,
            data: LogData,
            time: Long = System.currentTimeMillis()
        )
    }

    class LogData {
        val data = HashMap<String, String?>()
        infix fun String.to(value: String?) {
            data[this] = value.toString()
        }
    }
}
