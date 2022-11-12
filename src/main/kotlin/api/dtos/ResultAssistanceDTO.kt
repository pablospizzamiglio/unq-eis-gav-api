package api.dtos

import entity.Assistance

class ResultAssistanceDTO(val result: List<AssistanceSimpleDTO>) {
    companion object {
        fun fromModel(assistanceList: List<Pair<Assistance, Int>>): ResultAssistanceDTO {
            val simpleAssistanceDTO = assistanceList.map {
                AssistanceSimpleDTO.fromModel(it.first,it.second)
            }
            return ResultAssistanceDTO(simpleAssistanceDTO)
        }
    }
}
