package com.maricoolsapps.adminpart

import java.lang.Exception

sealed class MyDataState<out T> {

    class onLoaded<T>(val data: T): MyDataState<T>()

    class notLoaded(e: Exception): MyDataState<Nothing>()

    object isLoading : MyDataState<Nothing>()
}