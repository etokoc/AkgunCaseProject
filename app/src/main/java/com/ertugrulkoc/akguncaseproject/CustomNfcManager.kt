package com.ertugrulkoc.akguncaseproject

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.nfc.tech.NfcB
import android.nfc.tech.NfcF
import android.nfc.tech.NfcV
import com.ertugrulkoc.akguncaseproject.model.NfcCardModel

class CustomNfcManager(private var activity: Activity) {

    private var nfcAdapter: NfcAdapter
    private var pendingIntent: PendingIntent
    private var intent: Intent

    init {
        intent = Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent =
            PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_MUTABLE)
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }

    fun enableNfcDispatch() {
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    fun disableNfcDispatch() {
        nfcAdapter.disableForegroundDispatch(activity)
    }

    fun readNfcTag(intent: Intent, result: (NfcCardModel) -> Unit) {
        val nfcCardModel = NfcCardModel()
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action || NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            tag?.apply {
                nfcCardModel.uid = bytesToHexString(id)
                techList.forEach {
                    nfcCardModel.usedTechList.add(it)
                }
                result(nfcCardModel)
            }
        }
    }

    private fun bytesToHexString(bytes: ByteArray?): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes?.size?.times(2) ?: 0)
        bytes?.forEachIndexed { i, byte ->
            val v = byte.toInt() and 0xFF
            hexChars[i * 2] = hexArray[v.ushr(4)]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}