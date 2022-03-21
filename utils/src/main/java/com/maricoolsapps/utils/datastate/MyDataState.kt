package com.maricoolsapps.utils.datastate

import com.maricoolsapps.utils.others.Status
import java.lang.Exception

data class MyDataState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): MyDataState<T> {
            return MyDataState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): MyDataState<T> {
            return MyDataState(Status.ERROR, data, msg)
        }

        fun <T> loading(): MyDataState<T> {
            return MyDataState(Status.LOADING, null, null)
        }
    }
}