package ir.malv.logtest

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ir.malv.utils.Pulp
import ir.malv.utils.db.PulpItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Pulp.init(this)
            .setMainTag("MyApp")
            .setDatabaseEnabled(true)
            .getSavedLogs(this).observe(this,
                Observer {
                    Pulp.sout("Saved logs changed: $it")
                })

        Pulp.info("TEST", "This is a message") {
            "Key1" to "Value1"
        }

        text.setOnClickListener {
            Pulp.debug("Text", "Why a simple text was clicked?") {
                "View" to "TextView"
                "Text" to "Hello World!"
            }
        }
    }
}
