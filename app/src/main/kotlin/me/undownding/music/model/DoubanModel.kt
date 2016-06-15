package me.undownding.music.model

import me.undownding.music.ext.DataBaseExt
import me.undownding.music.ext.RetrofitFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by undownding on 16-6-12.
 */
class DoubanModel {
    companion object {

        private val api by lazy {
            RetrofitFactory.create("https://api.douban.com/", DoubanApi::class.java)
        }

        fun search(@Query("q") query: String = "", @Query("tag") tag: String = "",
                   @Query("start") offset: Int = 0,@Query("count") size: Int = 20): Observable<DoubanSearchResult> {
            val key = "douban:$query"
            if (DataBaseExt.db.exists(key)) {
                return Observable.just(DataBaseExt.db.getObject(key, DoubanSearchResult::class.java))
            } else {
                return api.search(query, tag, offset, size)
                        .map{
                            DataBaseExt.db.put(key, it)
                            it
                        }
            }
        }

    }

    private interface DoubanApi {
        @GET("/v2/music/search")
        fun search(
                @Query("q") query: String,
                @Query("tag") tag: String ,
                @Query("start") offset: Int,
                @Query("count") size: Int
        ): Observable<DoubanSearchResult>
    }

    class DoubanSearchResult: BaseModle() {

        /**
         * count : 1
         * start : 0
         * total : 1
         * musics : [{"rating":{"max":10,"average":"8.9","numRaters":137,"min":0},"author":[{"name":"μ's"}],"alt_title":"","image":"https://img1.doubanio.com/spic/s26294468.jpg","tags":[{"count":47,"name":"μ's"},{"count":33,"name":"ラブライブ！"},{"count":30,"name":"ACG"},{"count":12,"name":"日本"},{"count":8,"name":"Anime"},{"count":7,"name":"声優ソング"},{"count":6,"name":"OST"},{"count":5,"name":"2013"}],"mobile_link":"https://m.douban.com/music/subject/21327660/","attrs":{"publisher":["ランティス"],"singer":["μ's"],"discs":["1"],"pubdate":["2013-04-03"],"title":["No brand girls / START:DASH!!"],"media":["CD"],"tracks":["No brand girls","START:DASH!!","No brand girls (Off Vocal)","START:DASH!! (Off Vocal)"],"version":["Single","Maxi"]},"title":"No brand girls / START:DASH!!","alt":"https://music.douban.com/subject/21327660/","id":"21327660"}]
         */

        var count: Int = 0
        var start: Int = 0
        var total: Int = 0
        /**
         * rating : {"max":10,"average":"8.9","numRaters":137,"min":0}
         * author : [{"name":"μ's"}]
         * alt_title :
         * image : https://img1.doubanio.com/spic/s26294468.jpg
         * tags : [{"count":47,"name":"μ's"},{"count":33,"name":"ラブライブ！"},{"count":30,"name":"ACG"},{"count":12,"name":"日本"},{"count":8,"name":"Anime"},{"count":7,"name":"声優ソング"},{"count":6,"name":"OST"},{"count":5,"name":"2013"}]
         * mobile_link : https://m.douban.com/music/subject/21327660/
         * attrs : {"publisher":["ランティス"],"singer":["μ's"],"discs":["1"],"pubdate":["2013-04-03"],"title":["No brand girls / START:DASH!!"],"media":["CD"],"tracks":["No brand girls","START:DASH!!","No brand girls (Off Vocal)","START:DASH!! (Off Vocal)"],"version":["Single","Maxi"]}
         * title : No brand girls / START:DASH!!
         * alt : https://music.douban.com/subject/21327660/
         * id : 21327660
         */

        var musics: List<MusicsEntity>? = null

        class MusicsEntity {
            /**
             * max : 10
             * average : 8.9
             * numRaters : 137
             * min : 0
             */

            var rating: RatingEntity? = null
            var alt_title: String? = null
            var image: String? = null
            var mobile_link: String? = null
            var attrs: AttrsEntity? = null
            var title: String? = null
            var alt: String? = null
            var id: String? = null
            /**
             * name : μ's
             */

            var author: List<AuthorEntity>? = null
            /**
             * count : 47
             * name : μ's
             */

            var tags: List<TagsEntity>? = null

            class RatingEntity {
                var max: Int = 0
                var average: String? = null
                var numRaters: Int = 0
                var min: Int = 0
            }

            class AttrsEntity {
                var publisher: List<String>? = null
                var singer: List<String>? = null
                var discs: List<String>? = null
                var pubdate: List<String>? = null
                var title: List<String>? = null
                var media: List<String>? = null
                var tracks: List<String>? = null
                var version: List<String>? = null
            }

            class AuthorEntity {
                var name: String? = null
            }

            class TagsEntity {
                var count: Int = 0
                var name: String? = null
            }
        }
    }
}
