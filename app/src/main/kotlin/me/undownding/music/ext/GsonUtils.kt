package me.undownding.music.ext

import com.google.gson.Gson

/**
 * Created by undownding on 16-6-24.
 */
class GsonUtils {
    companion object {
        val instance = Gson()
    }
}

fun Any.toJson(): String {
    return GsonUtils.instance.toJson(this)
}