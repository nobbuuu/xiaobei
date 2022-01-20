package com.tcl.base.kt

import android.graphics.Bitmap
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.ImageLoader
import coil.bitmap.BitmapPool
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import coil.size.Size
import coil.size.ViewSizeResolver
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ImageUtils
import com.tcl.base.R

/**
 * @author : Yzq
 * time : 2020/11/28 0:29
 */
fun ImageView.loadRadius(imageUrl: String?, radius: Float = 8f, error: Int? = null) {
    loadRadius(imageUrl, radius, radius, radius, radius, error)
}

fun ImageView.loadGif(imageUrl: String, placeholder: Int? = null) {
    val url = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        imageUrl
    } else {
        imageUrl.split("?").getOrNull(0)
    }
    load(url, ImageLoader.Builder(context)
        .componentRegistry {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder())
            } else {
                add(GifDecoder())
            }
        }
        .build())
    {
        //transformations(LargeBitMapTransformation())  影响GIF
        size(ViewSizeResolver(this@loadGif))
        error(R.mipmap.ic_norm_goods)
        if (placeholder != null) {
            placeholder(placeholder)
        }
    }
}

fun ImageView.loadRadius(
    imageUrl: String?,
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomLeft: Float = 0f,
    bottomRight: Float = 0f,
    @DrawableRes error: Int? = null
) {
    val image = imageUrl ?: ""
    load(image) {
        size(ViewSizeResolver(this@loadRadius))
        val topLeftFloat = ConvertUtils.dp2px(topLeft).toFloat()
        val topRightFloat = ConvertUtils.dp2px(topRight).toFloat()
        val bottomLeftFloat = ConvertUtils.dp2px(bottomLeft).toFloat()
        val bottomRightFloat = ConvertUtils.dp2px(bottomRight).toFloat()
        transformations(
            RoundedCornersTransformation(
                topLeft = topLeftFloat,
                topRight = topRightFloat,
                bottomLeft = bottomLeftFloat,
                bottomRight = bottomRightFloat
            )
        )
        placeholder(R.mipmap.ic_norm_goods)
        error(error ?: R.mipmap.ic_norm_goods)
    }
}

class LargeBitMapTransformation : Transformation {

    override fun key(): String = LargeBitMapTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        return ImageUtils.bytes2Bitmap(ImageUtils.compressByQuality(input, 100))
    }

    override fun equals(other: Any?) = other is LargeBitMapTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "LargeBitMapTransformation()"

}