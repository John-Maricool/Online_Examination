package com.maricoolsapps.utilsandrepository.utils

import java.lang.Exception

sealed class MyDataState<out T> {

   data class onLoaded<out T>(val data: T): MyDataState<T>()
    data class notLoaded(val e: Exception): MyDataState<Nothing>()

    object isLoading : MyDataState<Nothing>()
}