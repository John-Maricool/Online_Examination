package com.maricoolsapps.adminpart

import com.maricoolsapps.adminpart.interfaces.Mapper
import com.maricoolsapps.adminpart.room.RoomEntity
import javax.inject.Inject

class CloudMapper
@Inject
constructor(): Mapper<RoomEntity, ServerQuizData>{
    override fun mapFromModel(model: ServerQuizData): RoomEntity {
        return RoomEntity(
                question = model.question,
                firstOption = model.firstOption,
                secondOption = model.secondOption,
                thirdOption = model.thirdOption,
                forthOption = model.forthOption,
                correctIndex = model.correctIndex
        )
    }

    override fun mapToModel(model: RoomEntity): ServerQuizData {
        return ServerQuizData(
                question = model.question,
                firstOption = model.firstOption,
                secondOption = model.secondOption,
                thirdOption = model.thirdOption,
                forthOption = model.forthOption,
                correctIndex = model.correctIndex
        )
    }

    fun convertToList(modelList: List<RoomEntity>): List<ServerQuizData>{
        return modelList.map { mapToModel(it) }
    }
}