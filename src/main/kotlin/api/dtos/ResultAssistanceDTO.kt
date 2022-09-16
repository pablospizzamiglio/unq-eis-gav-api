package api.dtos

import entity.Assistance

class ResultAssistanceDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(viajes: List<Assistance>): ResultAssistanceDTO {
            val simpleviajesDTOs = viajes.map {
                AssistanceSimpleDTO.fromModel(
                    it.id,
                    it.kind,
                    it.detail,
                    it.costPerKm,
                    it.assistant
                )
            }
            return ResultAssistanceDTO(simpleviajesDTOs)
        }
    }
}