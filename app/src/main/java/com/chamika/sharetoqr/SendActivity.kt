package com.chamika.sharetoqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit

class SendActivity : AppCompatActivity(R.layout.activity_send) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = mapBundle(intent)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragment_container_view, QrDisplayFragment::class.java, bundle)
            }
        }
    }

    private fun mapBundle(intent: Intent?): Bundle? {
        intent ?: throw IllegalArgumentException("Intent not found")
        val type = when (intent.type) {
            "text/plain" -> 1
            else -> throw IllegalArgumentException("Type: ${intent.type} not supported")
        }
        val value: String = intent.getStringExtra(Intent.EXTRA_TEXT)
            ?: throw IllegalArgumentException("Value not found")
        return bundleOf(
            QrDisplayFragment.EXTRA_TYPE to type,
            QrDisplayFragment.EXTRA_VALUE to value
        )
    }
}