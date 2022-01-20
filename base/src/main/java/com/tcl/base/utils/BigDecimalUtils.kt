package com.tcl.base.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * @author : Yzq
 * time : 2020/11/17 9:34
 */
object BigDecimalUtils {

    private val funm = createDecimalFormat()

    private val pattern = Pattern.compile("[^-0-9.]")


    /**
     * 金额相加
     * @method
     * @date: 2020/5/21 19:13
     * @author: moran
     * @param valueStr 基础值
     * @param addStr 被加数
     *
     * @return 金额
     */
    fun moneyAdd(valueStr: String, addStr: String): String {

        val value = BigDecimal(formatMoney(valueStr))

        val augend = BigDecimal(formatMoney(addStr))

        return funm.format(value.add(augend))

    }

    fun moneyAdd(valueStr: Double, addStr: Double): Double {

        val value = BigDecimal(valueStr.toString())

        val augend = BigDecimal(addStr.toString())

        return value.add(augend).toDouble()

    }

    fun moneyAdd(vararg values: Double): Double {
        var tempValue = 0.0
        values.forEach {
            tempValue = moneyAdd(tempValue.toString(), it.toString()).toDouble()
        }
        return tempValue
    }


    /**
     * 金额相加
     * @method
     * @date: 2020/5/21 19:13
     * @author: moran
     * @param valueStr 基础值
     * @param addStr 被加数
     * @param funm 金额格式化类
     *
     * @return 金额
     */
    fun moneyAdd(valueStr: String, addStr: String, funm: DecimalFormat): String {

        val value = BigDecimal(formatMoney(valueStr))

        val augend = BigDecimal(formatMoney(addStr))

        return funm.format(value.add(augend))

    }


    fun moneyAdd(valueStr: String, mulStr: String, roundingMode: Int = BigDecimal.ROUND_HALF_UP, scale: Int = 2): String {

        val value = BigDecimal(formatMoney(valueStr))
        val mulValue = BigDecimal(formatMoney(mulStr))

        val result = value.add(mulValue).setScale(scale, roundingMode)
        return result.toString()
    }

    /**
     * 金额相减
     * @method
     * @date: 2020/5/21 19:24
     * @author: moran
     * @param valueStr 基础值
     * @param minusStr 被减数
     *
     * @return 金额
     */
    fun moneySub(valueStr: String, minusStr: String): String {

        val value = BigDecimal(formatMoney(valueStr))

        val minuValue = BigDecimal(formatMoney(minusStr))

        return funm.format(value.subtract(minuValue))

    }

    fun moneySub(valueStr: Double, minusStr: Double): Double {

        val value = BigDecimal(valueStr.toString())

        val minuValue = BigDecimal(minusStr.toString())

        return value.subtract(minuValue).toDouble()

    }

    /**
     * 金额相减
     * @method
     * @date: 2020/5/21 19:24
     * @author: moran
     * @param valueStr 基础值
     * @param minusStr 被减数
     * @param funm 金额格式化类
     * @return 金额
     */
    fun moneySub(valueStr: String, minusStr: String, funm: DecimalFormat): String {

        val value = BigDecimal(formatMoney(valueStr))

        val minuValue = BigDecimal(formatMoney(minusStr))

        return funm.format(value.subtract(minuValue))
    }

    /**
     * 金额相乘
     * @method
     * @date: 2020/5/21 19:27
     * @author: moran
     * @param valueStr 基础值
     * @param mulStr 被乘数
     *
     * @return 金额
     */
    fun moneyMul(valueStr: String, mulStr: String): String {

        val value = BigDecimal(formatMoney(valueStr))

        val mulValue = BigDecimal(formatMoney(mulStr))
        val multiply = value.multiply(mulValue)
        return funm.format(multiply)

    }


    fun moneyMul(valueStr: Double, mulStr: Double): Double {

        val value = BigDecimal(valueStr)

        val mulValue = BigDecimal(mulStr)

        return value.multiply(mulValue).toDouble()

    }

    /**
     * 金额相除
     * @method
     * @date: 2020/5/21 19:29
     * @author: moran
     * @param valueStr 除数
     * @param divideStr 被除数
     * @param scale 小数点位数 默认为2
     * @param roundingMode 舍入模式:
     * 1.ROUND_CEILING  向正无穷方向舍入
     * 2.ROUND_DOWN  向零方向舍入
     * 3.ROUND_FLOOR 向负无穷方向舍入
     * 4.ROUND_HALF_DOWN 向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
     * 5.ROUND_HALF_EVEN  向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，如果保留位数是奇数，使用ROUND_HALF_UP，如果是偶数，使用ROUND_HALF_DOWN
     * 6.ROUND_HALF_UP    向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
     * 7.ROUND_UNNECESSARY  计算结果是精确的，不需要舍入模式
     * 8.ROUND_UP    向远离0的方向舍入
     *
     * @return 金额
     */
    fun moneyDiv(
        valueStr: String,
        divideStr: String,
        scale: Int = 2,
        roundingMode: Int = BigDecimal.ROUND_HALF_UP
    ): String {
        val value = BigDecimal(formatMoney(valueStr))
        val divideValue = BigDecimal(formatMoney(divideStr))
        return funm.format(value.divide(divideValue, scale, roundingMode))
    }


    fun moneyDiv2(
        valueStr: String,
        divideStr: String,
        scale: Int = 2,
        roundingMode: Int = BigDecimal.ROUND_HALF_UP
    ): String {
        val value = BigDecimal(formatMoney(valueStr))
        val divideValue = BigDecimal(formatMoney(divideStr))
        return value.divide(divideValue, scale, roundingMode).toString()
    }

    /**
     * 金额相乘
     * @method
     * @date: 2020/5/21 19:27
     * @author: moran
     * @param valueStr 基础值
     * @param mulStr 被乘数
     * @param funm 金额格式化类
     *
     * @return 金额
     */
    fun moneyMul(valueStr: String, mulStr: String, roundingMode: Int = BigDecimal.ROUND_HALF_UP, scale: Int = 2): String {

        val value = BigDecimal(formatMoney(valueStr))

        val mulValue = BigDecimal(formatMoney(mulStr))
        val result = value.multiply(mulValue).setScale(scale, roundingMode)
        return result.toString()
    }


    fun div(
        valueStr: Double,
        divideStr: Double,
        scale: Int = 2,
        roundingMode: Int = BigDecimal.ROUND_HALF_UP
    ): Double {
        val value = BigDecimal(valueStr)
        val divideValue = BigDecimal(divideStr)
        return value.divide(divideValue, scale, roundingMode).toDouble()
    }

    /**
     * 金额相除
     * @method
     * @date: 2020/5/21 19:29
     * @author: moran
     * @param valueStr 除数
     * @param divideStr 被除数
     * @param scale 小数点位数 默认为2
     * @param roundingMode 舍入模式:
     * 1.ROUND_CEILING  向正无穷方向舍入
     * 2.ROUND_DOWN  向零方向舍入
     * 3.ROUND_FLOOR 向负无穷方向舍入
     * 4.ROUND_HALF_DOWN 向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向下舍入, 例如1.55 保留一位小数结果为1.5
     * 5.ROUND_HALF_EVEN  向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，如果保留位数是奇数，使用ROUND_HALF_UP，如果是偶数，使用ROUND_HALF_DOWN
     * 6.ROUND_HALF_UP    向（距离）最近的一边舍入，除非两边（的距离）是相等,如果是这样，向上舍入, 1.55保留一位小数结果为1.6
     * 7.ROUND_UNNECESSARY  计算结果是精确的，不需要舍入模式
     * 8.ROUND_UP    向远离0的方向舍入
     * @param funm DecimalFormat 金额格式化
     *
     * @return 金额
     */

    fun moneyDiv(
        valueStr: String,
        divideStr: String,
        scale: Int = 2,
        roundingMode: Int = BigDecimal.ROUND_HALF_UP,
        funm: DecimalFormat
    ): String {
        val value = BigDecimal(formatMoney(valueStr))
        val divideValue = BigDecimal(formatMoney(divideStr))
        return funm.format(value.divide(divideValue, scale, roundingMode))
    }


    /**
     * 去除非数字内容，如：0.12元，0,01，1,11
     * @date: 2020/5/21 19:16
     * @author: moran
     * @param value 格式化金额类
     * @return
     */
    fun formatMoney(value: String?): String {

        if (null == value || "".equals(value)) {

            return "0.00"
        }
        val money = pattern.matcher(value).replaceAll("").trim()
        if ("".equals(money)) {
            return "0.00"
        }
        return money
    }


    /**
     * 格式化金额,
     * @method
     * @date: 2020/5/21 19:48
     * @author: moran
     * @param value 金额内容
     * @param pattern 金额格式化形式
     * @return 金额
     */
    fun decimalFormatMoney(value: String?, pattern: String = "0.00"): String {

        val format = DecimalFormat(pattern)

        return format.format(BigDecimal(formatMoney(value)))
    }


    /**
     * 创建金额格式化对象
     * @method
     * @date: 2020/5/21 19:50
     * @author: moran
     * @param
     * @return
     */
    private fun createDecimalFormat(
        pattern: String = "0.00",
        roundingMode: RoundingMode = RoundingMode.HALF_UP
    ): DecimalFormat {

        val funm = DecimalFormat(pattern)
        funm.roundingMode = roundingMode
        return funm
    }
}