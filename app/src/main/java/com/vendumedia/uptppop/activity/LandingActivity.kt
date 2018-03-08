package com.vendumedia.uptppop.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.Toast
import com.vendumedia.uptppop.R
import io.vrinda.kotlinpermissions.PermissionsActivity
import kotterknife.bindView

class LandingActivity : PermissionsActivity() {

    val pick : LinearLayout by bindView(R.id.buttonList)

    private var DOUBLE_BACK_TOEXIT = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        pick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        if (this.DOUBLE_BACK_TOEXIT) {

            super.onBackPressed()
            return
        }

        this.DOUBLE_BACK_TOEXIT = true
        Toast.makeText(this, "Tap sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ this.DOUBLE_BACK_TOEXIT = false }, 2000)
    }
}
