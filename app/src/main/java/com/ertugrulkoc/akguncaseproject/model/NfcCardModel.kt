package com.ertugrulkoc.akguncaseproject.model

data class NfcCardModel(
    var uid: String? = "",
    var usedTechList: ArrayList<String> = arrayListOf()
)