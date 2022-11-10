package api.dtos

import entity.Assistance

class ResultAssistanceDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(assistanceList: List<Assistance>): ResultAssistanceDTO {
            val simpleAssistanceDTO = assistanceList.map {
                AssistanceSimpleDTO.fromModel(it)
            }
            return ResultAssistanceDTO(simpleAssistanceDTO)
        }
    }
}
