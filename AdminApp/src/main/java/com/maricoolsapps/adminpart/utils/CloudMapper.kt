package com.maricoolsapps.adminpart.utils

import com.maricoolsapps.adminpart.interfaces.Mapper
import com.maricoolsapps.adminpart.models.ServerQuizDataModel
import com.maricoolsapps.adminpart.room.RoomEntity
import javax.inject.Inject

class CloudMapper(): Mapper<RoomEntity, ServerQuizDataModel>{
    override fun mapFromModel(model: ServerQuizDataModel): RoomEntity {
        return RoomEntity(
                question = model.question,
                firstOption = model.firstOption,
                secondOption = model.secondOption,
                thirdOption = model.thirdOption,
                forthOption = model.forthOption,
                correctIndex = model.correctIndex
        )
    }

    override fun mapToModel(model: RoomEntity): ServerQuizDataModel {
        return ServerQuizDataModel(
                question = model.question,
                firstOption = model.firstOption,
                secondOption = model.secondOption,
                thirdOption = model.thirdOption,
                forthOption = model.forthOption,
                correctIndex = model.correctIndex
        )
    }

    fun convertToList(modelList: List<RoomEntity>): List<ServerQuizDataModel>{
        return modelList.map { mapToModel(it) }
    }
}