package com.maricoolsapps.utilsandrepository.utils

interface Mapper<CacheModel, ServerModel> {

    fun mapFromModel(model: ServerModel): CacheModel
    fun mapToModel(model: CacheModel): ServerModel

}