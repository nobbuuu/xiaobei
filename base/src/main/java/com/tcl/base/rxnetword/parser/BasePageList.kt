package com.tcl.base.rxnetword.parser

class BasePageList<T> {
    var total //总条数
            = 0
    var data: List<T> = arrayListOf()
}