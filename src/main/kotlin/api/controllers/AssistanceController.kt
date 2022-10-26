package api.controllers

import api.dtos.ResultAssistanceDTO
import dao.HibernateAssistanceDAO
import entityManager
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context

class AssistanceController {
    fun findAll(ctx: Context) {
        val assistanceDAO = HibernateAssistanceDAO(ctx.entityManager)

        if (ctx.queryParam("kind") == null) {
            val assistanceRecords = assistanceDAO.findAll()
            val result = ResultAssistanceDTO.fromModel(assistanceRecords)
            ctx.json(result)
        } else {
            try {
                val kind = ctx.queryParamAsClass("kind", String::class.java)
                    .check({ c -> c.isNotBlank() }, "No parameters added").get()
                val assistance = assistanceDAO.findAllByKind(kind)
                val result = ResultAssistanceDTO.fromModel(assistance)
                ctx.json(result)
            } catch (e: BadRequestResponse) {
                throw BadRequestResponse(e.message!!)
            }
        }
    }
}
