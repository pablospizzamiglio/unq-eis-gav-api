package api.dtos

import entity.Assistance

class ResultAssistanceDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(assistanceList: List<Assistance>): ResultAssistanceDTO {
            val simpleAssistanceDTO = assistanceList.map {
                AssistanceSimpleDTO.fromModel(
                    it.id,
                    it.kind.toString(),
                    it.fixedCost,
                    it.costPerKm,
                    it.user
                )
            }
            return ResultAssistanceDTO(simpleAssistanceDTO)
        }
    }
}
