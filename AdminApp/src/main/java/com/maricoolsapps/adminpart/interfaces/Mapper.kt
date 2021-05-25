package com.maricoolsapps.adminpart.interfaces

interface Mapper<CacheModel, ServerModel> {

    fun mapFromModel(model: ServerModel): CacheModel
    fun mapToModel(model: CacheModel): ServerModel

}