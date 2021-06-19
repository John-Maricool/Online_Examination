package com.maricoolsapps.utils

import java.lang.Exception

sealed class MyDataState {

   data class onLoaded(val data: Any): MyDataState()
    data class notLoaded(val e: Exception): MyDataState()

    object isLoading : MyDataState()
}