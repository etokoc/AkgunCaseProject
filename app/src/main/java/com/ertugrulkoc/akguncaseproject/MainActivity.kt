package com.ertugrulkoc.akguncaseproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertugrulkoc.akguncaseproject.data.adapter.CardsAdapter
import com.ertugrulkoc.akguncaseproject.databinding.ActivityMainBinding
import com.ertugrulkoc.akguncaseproject.databinding.BottomDialogBinding
import com.ertugrulkoc.akguncaseproject.util.SharedPrefencesUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var customNfcManager: CustomNfcManager
    private lateinit var sharedPrefencesUtil: SharedPrefencesUtil
    private lateinit var cardsAdapter: CardsAdapter
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        sharedPrefencesUtil = SharedPrefencesUtil(this)
        customNfcManager = CustomNfcManager(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        cardsAdapter = CardsAdapter()
        binding.recyclerviewCard.adapter = cardsAdapter
        binding.recyclerviewCard.layoutManager = LinearLayoutManager(this)
        updateRecylerView()
    }

    private fun updateRecylerView() {
        cardsAdapter.setCardList(
            ArrayList<String>(
                sharedPrefencesUtil.getCardsFromLocal().toList()
            )
        )
    }

    /**
     * Device Nfc Supported Control & Nfc Open-Close Control
     * */
    override fun onResume() {
        super.onResume()
        if (this::customNfcManager.isInitialized) {
            if (customNfcManager.isNfcSupported()) {
                if (customNfcManager.isNfcEnabled()) {
                    customNfcManager.enableNfcDispatch()
                } else {
                    openNfcSettings()
                }
            } else {
                // NFC IS NOT SUPPORTED FROM THIS DEVICE
                openDeviceNotSupportedWindow()
            }
        }
    }

    //Device Supported Control
    private fun openDeviceNotSupportedWindow() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.this_device_not_supported_nfc_technology))
            .setTitle(getString(R.string.warning))
            .setNeutralButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                }
            })
    }

    /**
     * This function has a show dialog. For When Nfc isn't enable, open the nfc settings.
     */
    private fun openNfcSettings() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.nfc_isn_t_enable_please_check_your_nfc_settings))
            .setTitle(getString(R.string.warning))
            .setPositiveButton("Open Settings", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                    val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                    startActivity(intent)
                }
            })
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.dismiss()
                }

            }).create().show()
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
                cardSavedToLocal(manager.uid)
            }
        }
    }

    private fun cardSavedToLocal(uid: String?) {
        if (sharedPrefencesUtil.isCardSaved(uid.toString())) {
            //Card is already exist
            AlertDialog.Builder(this).setTitle(getString(R.string.warning))
                .setMessage("This card already exist").show()
        } else {
            //Card isn't exists in local
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(BottomDialogBinding.inflate(layoutInflater).root)
            dialog.findViewById<TextView>(R.id.txtCardId)?.text = uid
            dialog.show()
            dialog.findViewById<MaterialButton>(R.id.btnCardSaved)?.setOnClickListener {
                //Card Save Button Listener
                sharedPrefencesUtil.addCard(uid.toString())
                updateRecylerView()
                dialog.dismiss()
            }
        }
    }
}
