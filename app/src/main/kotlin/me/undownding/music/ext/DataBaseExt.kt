package me.undownding.music.ext

import com.snappydb.DB
import com.snappydb.DBFactory
import me.undownding.music.MusicApplication

/**
 * Created by undownding on 16-6-16.
 */
class DataBaseExt {
    companion object {
        private var dbInstance = DBFactory.open(MusicApplication.instance)

        val db: DB
            get() {
                if (!dbInstance.isOpen) {
                    dbInstance = DBFactory.open(MusicApplication.instance)
                }
                return dbInstance
            }
    }
}