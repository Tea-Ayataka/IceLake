package net.ayataka.marinetooler

import com.google.gson.annotations.SerializedName

class Config {
    @SerializedName("action_order")
    val actionOrders = hashMapOf<String, Int>()
}