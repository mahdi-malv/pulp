package ir.malv.logtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ir.malv.utils.LogCatHandler
import ir.malv.utils.Pulp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Pulp.init(this)
            .setDatabaseEnabled(true)
            .addHandler(LogCatHandler())

        Pulp.info("TEST", "This is a message") {
            "Key1" to "Value1"
            "Key2" to null
        }

        text.setOnClickListener {
            Pulp.debug("Text", "Why a simple text was clicked?") {
                "View" to "TextView"
                "Text" to "Hello World!"
            }
        }
    }
}
