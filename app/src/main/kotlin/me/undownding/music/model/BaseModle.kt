package me.undownding.music.model

import com.google.gson.Gson

/**
 * Created by undownding on 16-6-12.
 */
open class BaseModle {

    companion object {
        val gson by lazy { Gson() }
    }

    override fun toString(): String {
        return gson.toJson(this)
    }
}