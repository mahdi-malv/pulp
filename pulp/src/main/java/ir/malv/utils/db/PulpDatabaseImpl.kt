package ir.malv.utils.db

import androidx.lifecycle.LiveData
import android.content.Context
import ir.malv.utils.Pulp
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter

object PulpDatabaseImpl {

    fun insert(context: Context, level: Pulp.Level, tags: List<String>, message: String, throwable: Throwable?, data: Pulp.LogData, time: Long) {
        Thread {
            PulpDatabase.database(context).pulpDao().insert(
                PulpItem(
                    level = level.toString(),
                    tags = tags.joinToString(","),
                    message = message,
                    error = errorToString(throwable),
                    data = serializeData(data.data),
                    time = time
                )
            )
        }.start()
    }

    fun savedLogs(context: Context): LiveData<List<PulpItem>> = PulpDatabase.database(context).pulpDao().getAll()

    fun clearLogs(context: Context) {
        Thread {
            PulpDatabase.database(context).pulpDao().clearAll()
        }.start()
    }


    private fun errorToString(throwable: Throwable?): String {
        return if (throwable != null) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            throwable.printStackTrace(pw)
            sw.toString()
        } else { "" }
    }

    private fun serializeData(data: Map<String, String>?): String = data?.let { JSONObject(it).toString() } ?: ""

}