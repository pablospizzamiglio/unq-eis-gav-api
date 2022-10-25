package api.controllers

import api.dtos.ResultAssistanceDTO
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.AssistanceServiceImpl

class AssistanceController(private val assistanceService: AssistanceServiceImpl) {

    fun findAll(ctx: Context) {
        if (ctx.queryParam("kind") == null) {
            val assistances = assistanceService.findAll()
            val result = ResultAssistanceDTO.fromModel(assistances)
            ctx.json(result)
        } else {
            try {
                val kind = ctx.queryParamAsClass("kind", String::class.java)
                    .check({ c -> !c.isNullOrBlank() }, "No parameters added").get()
                val assistances = assistanceService.findAllByKind(kind)
                val result = ResultAssistanceDTO.fromModel(assistances)
                ctx.json(result)
            } catch (e: BadRequestResponse) {
                throw BadRequestResponse(e.message!!)
            }
        }
    }
}
