package api.dtos.controllers.dtos

import entity.Assistance

class ResultAssistencesDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(viajes: List<Assistance>): ResultAssistencesDTO {
            val simpleviajesDTOs = viajes.map {
                AssistanceSimpleDTO.fromModel(
                    it.id,
                    it.kind,
                    it.detail,
                    it.costPerKm,
                    it.assistant
                )
            }
            return ResultAssistencesDTO(simpleviajesDTOs)
        }
    }
}