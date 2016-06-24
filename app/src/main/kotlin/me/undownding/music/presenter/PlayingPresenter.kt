package me.undownding.music.presenter

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.baidu.music.model.Music
import me.undownding.music.MusicApplication
import me.undownding.music.PlayingActivity
import me.undownding.music.ext.BitmapUtils
import me.undownding.music.ext.FrescoExt
import me.undownding.music.ext.GsonUtils
import me.undownding.music.service.PlayingService
import me.undownding.music.service.RxPlayingServiceImpl
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by undownding on 16-6-24.
 */
class PlayingPresenter(activity: PlayingActivity) {

    companion object {
        val LOOP_TIME: Long = 1000

        fun getTime(ms: Long): String {
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(ms),
                    TimeUnit.MILLISECONDS.toSeconds(ms) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms))
            )
        }
    }

    val activity = activity
    val rxBinding by lazy { RxPlayingServiceImpl() }
    var service: PlayingService? = null
    val loopThread by lazy { LoopThread(activity) }

    fun rxBlur(bkg: Bitmap, view: ImageView) {
        Observable.create<BitmapDrawable> {
            val rs = RenderScript.create(MusicApplication.instance)
            val radius: Float = 20.toFloat();
            val overlay = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(overlay);
            canvas.translate((-view.left).toFloat(), (-view.top).toFloat())
            canvas.drawBitmap(BitmapUtils.scaleCenterCrop(bkg, view.measuredHeight, view.measuredWidth), 0.toFloat(), 0.toFloat(), null)

            val overlayAlloc = Allocation.createFromBitmap(rs, overlay)
            val blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.element)

            blur.setInput(overlayAlloc)
            blur.setRadius(radius)
            blur.forEach(overlayAlloc)
            overlayAlloc.copyTo(overlay)

            rs.destroy()

            // draw a 0x7f000000 overlay
            val paint = Paint()
            val rect = Rect(0, 0, view.measuredWidth, view.measuredHeight)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#7f000000")
            canvas.drawRect(rect, paint)

            it.onNext(BitmapDrawable(MusicApplication.instance.resources, overlay))
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.setImageDrawable(it)
                }
    }

    fun doBind() {
        activity.startService(Intent(activity, PlayingService::class.java))
        rxBinding.bind(activity)
                .subscribeOn(Schedulers.newThread())
                .subscribe {
                    service = it
                    loopThread.start()
                    handleIntent(activity.intent)
                }

        if (activity.intent.getBooleanExtra(PlayingActivity.PLAY_MUSIC, false)) {
            val music = GsonUtils.instance.fromJson(activity.intent.getStringExtra(PlayingActivity.MUSIC_BEAN), Music::class.java)


            Observable.create<Bitmap> { subscriber ->
                val uri = Uri.parse(music.mPicBig)
                if (FrescoExt.isDownloaded(uri)) {
                    subscriber.onNext(FrescoExt.getOfflineImage(uri))
                    subscriber.onCompleted()
                } else {
                    Volley.newRequestQueue(MusicApplication.instance)
                            .add(ImageRequest(music.mPicBig, {
                                subscriber.onNext(it)
                                subscriber.onCompleted()
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, {
                                subscriber.onCompleted()
                            }))
                }
            }
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        activity.picAlbum.setImageBitmap(it)
                        rxBlur(it, activity.rootView)
                    }
            activity.tvTitle.text = music.mTitle
            activity.tvArtist.text = music.mArtist
            activity.slider.min = 0
            activity.slider.max = music.mFileDuration.toInt()
            activity.tvCurrentTime.text = getTime(0)
        }
    }

    fun doUnbind() {
        if (service != null) {
            activity.unbindService(rxBinding.serviceConnection)
            loopThread.stop = true
        }
    }

    fun handleIntent(intent: Intent) {
        if (intent.getBooleanExtra(PlayingActivity.PLAY_MUSIC, false)) {
            val music = GsonUtils.instance.fromJson(intent.getStringExtra(PlayingActivity.MUSIC_BEAN), Music::class.java)

            if (!(service?.player?.mCurrentList?.contains(music)?: false)) {
                service?.player?.mCurrentList?.add(music)
            }
            service?.player?.mCurrentList?.add(music)
            service?.player?.reset()
            service?.player?.setOnPreparedListener {
                service?.player?.play(music.mId.toLong())
                service?.player?.setOnPreparedListener(null)
            }
            service?.player?.prepare(music)


            intent.putExtra(PlayingActivity.PLAY_MUSIC, false)
            activity.intent = intent
        } else {
            // dispaly image from service
        }
    }


    fun changeValue(value: Int) {
        service?.player?.seek(value)
    }

    class LoopThread(activity: PlayingActivity): Thread() {
        var stop = false
        val activity = activity

        override fun run() {
            while (!stop) {
                activity.runOnUiThread{
                    activity.slider.value = activity.presenter.service?.player?.position() ?: 0
                    activity.tvCurrentTime.text = getTime(activity.slider.value.toLong())
                    if (activity.slider.max == 0) {
                        activity.slider.max = activity.presenter.service?.player?.duration() ?: 0
                        activity.tvTotalTime.text = getTime(activity.slider.max.toLong())
                    }
                }
                sleep(LOOP_TIME)
            }
        }
    }
}