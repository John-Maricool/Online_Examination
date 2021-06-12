package com.maricoolsapps.utilsandrepository.utils

import com.maricoolsapps.localdatabase.room.RoomEntity
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import javax.inject.Inject

class CloudMapper : Mapper<RoomEntity, ServerQuizDataModel>{
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

    fun convertToServerList(modelList: List<RoomEntity>): List<ServerQuizDataModel>{
        return modelList.map { mapToModel(it) }
    }

    fun convertToLocalList(modelList: List<ServerQuizDataModel>): List<RoomEntity>{
        return modelList.map { mapFromModel(it) }
    }
}