package me.undownding.music.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest

/**
 * Created by undownding on 16-6-15.
 */
class FrescoExt {
    companion object {
        fun isDownloaded(uri: Uri): Boolean {
            val request = ImageRequest.fromUri(uri)
            val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request)
            return ImagePipelineFactory.getInstance().mainDiskStorageCache.hasKey(cacheKey)
        }

        fun getOfflineImage(uri: Uri): Bitmap? {
            if (isDownloaded(uri)) {
                val request = ImageRequest.fromUri(uri)
                val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request)
                val resource = ImagePipelineFactory.getInstance().mainDiskStorageCache.getResource(cacheKey)
                return BitmapFactory.decodeFile((resource as FileBinaryResource).file.absolutePath)
            } else {
                return null
            }
        }
    }
}