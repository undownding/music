package me.undownding.music.ext

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by undownding on 16-6-13.
 */
class RetrofitFactory {
    companion object {
        fun <T> create(url: String, clz: Class<T>): T {
            return Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(clz)
        }
    }
}
