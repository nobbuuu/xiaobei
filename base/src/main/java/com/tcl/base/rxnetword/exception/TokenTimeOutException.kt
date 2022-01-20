package com.tcl.base.rxnetword.exception

class TokenTimeOutException(val code: String, override val message: String?) :
    Exception()
