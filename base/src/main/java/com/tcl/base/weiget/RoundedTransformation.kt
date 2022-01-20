package com.tcl.base.weiget

import android.graphics.*
import androidx.annotation.Px
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.decode.DecodeUtils
import coil.size.OriginalSize
import coil.size.PixelSize
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.roundToInt


class RoundedTransformation(
    @Px private val topLeft: Float = 0f,
    @Px private val topRight: Float = 0f,
    @Px private val bottomLeft: Float = 0f,
    @Px private val bottomRight: Float = 0f
) : Transformation {

    constructor(@Px radius: Float) : this(radius, radius, radius, radius)

    init {
        require(topLeft >= 0 && topRight >= 0 && bottomLeft >= 0 && bottomRight >= 0) { "All radii must be >= 0." }
    }

    override fun key() =
        "${coil.transform.RoundedCornersTransformation::class.java.name}-$topLeft,$topRight,$bottomLeft,$bottomRight"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val outputWidth: Int
        val outputHeight: Int
        when (size) {
            is PixelSize -> {
                val multiplier = DecodeUtils.computeSizeMultiplier(
                    srcWidth = input.width,
                    srcHeight = input.height,
                    dstWidth = size.width,
                    dstHeight = size.height,
                    scale = Scale.FILL
                )
                outputWidth = (size.width / multiplier).roundToInt()
                outputHeight = (size.height / multiplier).roundToInt()
            }
            is OriginalSize -> {
                outputWidth = input.width
                outputHeight = input.height
            }
        }

        val output = pool.get(outputWidth, outputHeight, input.safeConfig)
        output.applyCanvas {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            val matrix = Matrix()
            matrix.setTranslate(
                (outputWidth - input.width) / 2f,
                (outputHeight - input.height) / 2f
            )
            val shader = BitmapShader(input, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(matrix)
            paint.shader = shader

            val radii = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft
            )
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
            val path = Path().apply { addRoundRect(rect, radii, Path.Direction.CW) }
            drawPath(path, paint)
        }

        return output
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is RoundedTransformation &&
                topLeft == other.topLeft &&
                topRight == other.topRight &&
                bottomLeft == other.bottomLeft &&
                bottomRight == other.bottomRight
    }

    override fun hashCode(): Int {
        var result = topLeft.hashCode()
        result = 31 * result + topRight.hashCode()
        result = 31 * result + bottomLeft.hashCode()
        result = 31 * result + bottomRight.hashCode()
        return result
    }

    override fun toString(): String {
        return "RoundedCornersTransformation(topLeft=$topLeft, topRight=$topRight, " +
                "bottomLeft=$bottomLeft, bottomRight=$bottomRight)"
    }
}

internal val Bitmap.safeConfig: Bitmap.Config
    get() = config ?: Bitmap.Config.ARGB_8888