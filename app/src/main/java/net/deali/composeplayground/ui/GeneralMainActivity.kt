package net.deali.composeplayground.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import net.deali.composeplayground.R
import net.deali.composeplayground.ui.main.MainActivity

class GeneralMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_main)

        findViewById<View>(R.id.tvGoToMain).setOnClickListener {
            Intent(this, MainActivity::class.java).let {
                startActivity(it)
            }
        }
    }
}