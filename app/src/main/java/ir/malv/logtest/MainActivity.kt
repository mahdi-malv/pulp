package ir.malv.logtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ir.malv.utils.Pulp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Pulp.info("TEST", "This is a message") {
            "Key1" to "Value1"
        }
    }
}
