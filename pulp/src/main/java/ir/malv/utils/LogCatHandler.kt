package ir.malv.utils

import android.util.Log

class LogCatHandler : Pulp.LogHandler {
    override fun onLog(
        level: Pulp.Level,
        tags: List<String>,
        message: String,
        t: Throwable?,
        data: Pulp.LogData,
        time: Long
    ) {
        val logMessage = logMessage(message, data)
        when (level) {
            Pulp.Level.I -> if (t != null) Log.i(tag(tags), logMessage, t) else Log.i(
                tag(tags),
                logMessage
            )
            Pulp.Level.D -> if (t != null) Log.d(tag(tags), logMessage, t) else Log.d(
                tag(tags),
                logMessage
            )
            Pulp.Level.W -> if (t != null) Log.w(tag(tags), logMessage, t) else Log.w(
                tag(tags),
                logMessage
            )
            Pulp.Level.E -> if (t != null) Log.e(tag(tags), logMessage, t) else Log.e(
                tag(tags),
                logMessage
            )
            Pulp.Level.WTF -> if (t != null) Log.wtf(tag(tags), logMessage, t) else Log.wtf(
                tag(tags),
                logMessage
            )
        }
    }

    private fun tag(tags: List<String>): String {
        return if (tags.size == 1) tags[0]
        else "(${tags.joinToString(",")})"
    }

    private fun logMessage(
        message: String,
        logData: Pulp.LogData
    ): String {
        val str = StringBuilder(message)
        if (logData.data.isNotEmpty()) {
            str.append(" {" + logData.data.map { "${it.key}=${it.value}" }.joinToString(", ") + "}")
        }
        return str.toString()
    }

}