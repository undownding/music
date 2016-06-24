package me.undownding.music.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

/**
 * Created by undownding on 16-6-24.
 */
class BitmapUtils {
    companion object {
        fun scaleCenterCrop(source: Bitmap, newHeight: Int, newWidth: Int): Bitmap {
            val sourceWidth = source.width
            val sourceHeight = source.height

            // Compute the scaling factors to fit the new height and width, respectively.
            // To cover the final image, the final scaling will be the bigger
            // of these two.
            val xScale = newWidth.toFloat() / sourceWidth
            val yScale = newHeight.toFloat() / sourceHeight
            val scale = Math.max(xScale, yScale)

            // Now get the size of the source bitmap when scaled
            val scaledWidth = scale * sourceWidth
            val scaledHeight = scale * sourceHeight

            // Let's find out the upper left coordinates if the scaled bitmap
            // should be centered in the new size give by the parameters
            val left = (newWidth - scaledWidth) / 2
            val top = (newHeight - scaledHeight) / 2

            // The target rectangle for the new, scaled version of the source bitmap will now
            // be
            val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            val dest = Bitmap.createBitmap(newWidth, newHeight, source.config)
            val canvas = Canvas(dest)
            canvas.drawBitmap(source, null, targetRect, null)

            return dest
        }
    }
}
