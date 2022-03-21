package com.maricoolsapps.room_library.room


class CloudMapper : Mapper<RoomEntity, ServerQuizDataModel>{
    override fun mapFromModel(model: ServerQuizDataModel): RoomEntity {
        return RoomEntity(
                question = model.question,
                firstOption = model.firstOption,
                image = model.image,
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
                image = model.image,
                secondOption = model.secondOption,
                thirdOption = model.thirdOption,
                forthOption = model.forthOption,
                correctIndex = model.correctIndex
        )
    }

    fun convertToList(modelList: List<RoomEntity>): List<ServerQuizDataModel>{
        return modelList.map { mapToModel(it) }
    }

    fun convertToRoomList(modelList: List<ServerQuizDataModel>): List<RoomEntity>{
        return modelList.map { mapFromModel(it) }
    }

}