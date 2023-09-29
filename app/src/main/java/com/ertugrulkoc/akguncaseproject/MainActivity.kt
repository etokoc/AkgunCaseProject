package com.ertugrulkoc.akguncaseproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ertugrulkoc.akguncaseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var customNfcManager: CustomNfcManager
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        customNfcManager = CustomNfcManager(this)
    }

    override fun onResume() {
        super.onResume()
        if (this::customNfcManager.isInitialized) {
            customNfcManager.enableNfcDispatch()
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::customNfcManager.isInitialized) {
            customNfcManager.disableNfcDispatch()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (this::customNfcManager.isInitialized) {
            customNfcManager.readNfcTag(intent) { manager ->
                binding.textview.text = manager.uid
            }
        }
    }
}
