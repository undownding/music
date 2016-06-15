package me.undownding.music.model

import android.util.Log
import com.baidu.music.model.Album
import com.baidu.music.model.Music
import com.baidu.music.onlinedata.OnlineManagerEngine
import com.baidu.music.onlinedata.PlayinglistManager
import me.undownding.music.MusicApplication
import me.undownding.music.ext.DataBaseExt
import me.undownding.music.ext.RetrofitFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by undownding on 16-6-11.
 */
class AlbumModel {

    companion object {
        private val oldApi by lazy { RetrofitFactory.create("http://tingapi.ting.baidu.com", BaiduInternalApi::class.java) }

        val ALBUM_IDS: Array<Long> = arrayOf(
                243493597, 243489870, 242437110, 243736140, 243492528, 243491969, 243491672, 243491327, 243490890, 244328089,
                72626701, 243490673, 243490321, 243490126, 244326919, 72693791, 244478101, 25197395, 243721448, 26633241,
                243666328, 38671304
        )

        fun requestAlbums(): Observable<List<com.baidu.music.model.Album>> {
            if (DataBaseExt.db.exists("all_albums")) {
                val result = ArrayList<com.baidu.music.model.Album>()
                result.addAll(DataBaseExt.db.getObjectArray("all_albums", com.baidu.music.model.Album::class.java))
                return Observable.just(result)
            } else {
                return Observable.from(ALBUM_IDS)
                        .flatMap {
                            requestAlbumById(it)
                        }
                        .subscribeOn(Schedulers.newThread())
                        .flatMap { result ->
                            Observable.create<com.baidu.music.model.Album> {
                                val search = DoubanModel.search(query = result.mTitle, size = 1).toBlocking().single()
                                if (search.musics?.size ?: 0 > 0) {
                                    result.mPicBig = search.musics?.get(0)?.image?.replace("/spic", "/lpic")
                                }
                                it.onNext(result)
                                it.onCompleted()
                            }
                        }
                        .toList()
                        .map{
                            DataBaseExt.db.put("all_albums", it)
                            it
                        }
            }
        }

        fun requestAlbumById(id: Long, withSongList: Boolean = false): Observable<com.baidu.music.model.Album> {
            val key = "album:$id"
            if (DataBaseExt.db.exists(key)) {
                return Observable.just(
                        DataBaseExt.db.get(key, com.baidu.music.model.Album::class.java)
                )
            }

            val task = Observable.create<com.baidu.music.model.Album> {
                it.onNext(
                        OnlineManagerEngine.getInstance(MusicApplication.instance)
                                .getAlbumManager(MusicApplication.instance)
                                .getLosslessAlbum(id))
                it.onCompleted()
            }

            val zipTask = Observable.zip(task, requestMusics(id)) { album, musics ->
                if (album.items == null) {
                    album.items = ArrayList()
                }
                if (album.items.size == 0) {
                    for (song  in musics.songlist!!) {
                        val music = Music()
                        music.mId = song.song_id
                        music.mArtist = song.author
                        music.mAlbumTitle = song.album_title
                        music.mTitle = song.title
                        music.mAlbumNo = song.album_no
                        album.items.add(music)
                    }
                }
                DataBaseExt.db.put(key, album)
                album
            }

            return if (withSongList) zipTask else task
        }

        fun requestMusics(id: Long): Observable<Album> {
            return oldApi.requestAlbum(albumId = id)
        }

        private interface BaiduInternalApi {
            // http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json&album_id=243493597
            @GET("/v1/restserver/ting")
            fun requestAlbum(
                    @Query("from") from: String = "qianqian",
                    @Query("version") version: String = "2.1.0",
                    @Query("method") method: String = "baidu.ting.album.getAlbumInfo",
                    @Query("format") format: String = "json",
                    @Query("album_id") albumId: Long): Observable<Album>

        }


        class Album {

            /**
             * album_id : 243493597
             * author : μ's
             * title : 僕たちはひとつの光 / Future style
             * publishcompany : 未知
             * prodcompany :
             * country : 日本
             * language : 日语
             * songs_total : 4
             * info : 2015年公開の劇場版『ラブライブ!The School Idol Movie』のシングル3週連続リリース第3弾。
             * styles : 影视原声,流行
             * style_id : 2
             * publishtime : 2015-07-15
             * artist_ting_uid : 103284482
             * all_artist_ting_uid : null
             * gender : 2
             * area : 5
             * pic_small : http://musicdata.baidu.com/data2/pic/243493748/243493748.jpg
             * pic_big : http://musicdata.baidu.com/data2/pic/243493745/243493745.jpg
             * hot :
             * favorites_num : null
             * recommend_num : null
             * artist_id : 56468493
             * all_artist_id : 56468493
             * pic_radio : http://musicdata.baidu.com/data2/pic/243493741/243493741.jpg
             * pic_s500 : http://musicdata.baidu.com/data2/pic/243493758/243493758.jpg
             * pic_s1000 : http://a.hiphotos.baidu.com/ting/pic/item/9213b07eca806538cc34332b91dda144ad348228.jpg
             */

            var albumInfo: AlbumInfoBean? = null
            /**
             * artist_id : 56468493
             * all_artist_id : 56468493
             * all_artist_ting_uid : 103284482
             * language : 日语
             * publishtime : 2015-07-15
             * album_no : 1
             * versions : 影视原声
             * pic_big : http://musicdata.baidu.com/data2/pic/243493745/243493745.jpg
             * pic_small : http://musicdata.baidu.com/data2/pic/243493748/243493748.jpg
             * hot : 405
             * file_duration : 0
             * del_status : 0
             * resource_type : 2
             * copy_type : 1
             * has_mv_mobile : 0
             * all_rate : 64,128,192,256,320
             * toneid : 0
             * country : 日本
             * area : 5
             * lrclink : http://musicdata.baidu.com/data2/lrc/be1be2950749b7ce1ecc7abc81fdd5aa/263948404/263948404.lrc
             * bitrate_fee : {"0":"0|0","1":"0|0"}
             * song_id : 243493591
             * title : 僕たちはひとつの光
             * ting_uid : 103284482
             * author : μ's
             * album_id : 243493597
             * album_title : 僕たちはひとつの光 / Future style
             * is_first_publish : 0
             * havehigh : 2
             * charge : 0
             * has_mv : 0
             * learn : 0
             * song_source : web
             * piao_id : 0
             * korean_bb_song : 0
             * resource_type_ext : 0
             * mv_provider : 0000000000
             */

            var songlist: List<SonglistBean>? = null

            class AlbumInfoBean {
                var album_id: String? = null
                var author: String? = null
                var title: String? = null
                var publishcompany: String? = null
                var prodcompany: String? = null
                var country: String? = null
                var language: String? = null
                var songs_total: String? = null
                var info: String? = null
                var styles: String? = null
                var style_id: String? = null
                var publishtime: String? = null
                var artist_ting_uid: String? = null
                var all_artist_ting_uid: Any? = null
                var gender: String? = null
                var area: String? = null
                var pic_small: String? = null
                var pic_big: String? = null
                var hot: String? = null
                var favorites_num: Any? = null
                var recommend_num: Any? = null
                var artist_id: String? = null
                var all_artist_id: String? = null
                var pic_radio: String? = null
                var pic_s500: String? = null
                var pic_s1000: String? = null
            }

            class SonglistBean {
                var artist_id: String? = null
                var all_artist_id: String? = null
                var all_artist_ting_uid: String? = null
                var language: String? = null
                var publishtime: String? = null
                var album_no: String? = null
                var versions: String? = null
                var pic_big: String? = null
                var pic_small: String? = null
                var hot: String? = null
                var file_duration: String? = null
                var del_status: String? = null
                var resource_type: String? = null
                var copy_type: String? = null
                var has_mv_mobile: Int = 0
                var all_rate: String? = null
                var toneid: String? = null
                var country: String? = null
                var area: String? = null
                var lrclink: String? = null
                var bitrate_fee: String? = null
                var song_id: String? = null
                var title: String? = null
                var ting_uid: String? = null
                var author: String? = null
                var album_id: String? = null
                var album_title: String? = null
                var is_first_publish: Int = 0
                var havehigh: Int = 0
                var charge: Int = 0
                var has_mv: Int = 0
                var learn: Int = 0
                var song_source: String? = null
                var piao_id: String? = null
                var korean_bb_song: String? = null
                var resource_type_ext: String? = null
                var mv_provider: String? = null
            }
        }

    }

}