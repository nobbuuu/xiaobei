package com.tcl.base.rxnetword.exception

/**
 *Author:tiaozi
 *DATE: 2021/7/31
 *DRC:
 */
class MultiDevicesException(val code: String, val data: String, override val message: String) :
    Exception()