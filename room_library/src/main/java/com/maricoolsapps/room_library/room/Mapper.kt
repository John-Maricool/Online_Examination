package com.maricoolsapps.room_library.room

interface Mapper<CacheModel, ServerModel> {

    fun mapFromModel(model: ServerModel): CacheModel
    fun mapToModel(model: CacheModel): ServerModel

}