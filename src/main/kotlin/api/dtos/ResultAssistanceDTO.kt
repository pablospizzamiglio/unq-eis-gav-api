package api.dtos

import entity.Assistance

class ResultAssistanceDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(viajes: List<Assistance>): ResultAssistanceDTO {
            val simpleviajesDTOs = viajes.map {
                AssistanceSimpleDTO.fromModel(
                    it.id,
                    it.kind.toString(),
                    it.fixedCost,
                    it.costPerKm,
                    it.user
                )
            }
            return ResultAssistanceDTO(simpleviajesDTOs)
        }
    }
}