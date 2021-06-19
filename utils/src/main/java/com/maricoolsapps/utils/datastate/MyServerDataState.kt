package com.maricoolsapps.utils.datastate

import java.lang.Exception

sealed class MyServerDataState{

    object onLoaded: MyServerDataState()

    data class notLoaded(val e: Exception): MyServerDataState()

    object isLoading : MyServerDataState()
}