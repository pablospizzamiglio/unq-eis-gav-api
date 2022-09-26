package api.controllers

import api.dtos.ResultAssistanceDTO
import dao.HibernateAssistanceDAO
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import transaction.TransactionRunner.runTrx

class AssistanceController(private val assistanceDAO: HibernateAssistanceDAO) {
    fun findAll(ctx: Context) {
        if (ctx.queryParam("kind") == null) {
            val assistances = runTrx {
                assistanceDAO.findAll()
            }
            val result = ResultAssistanceDTO.fromModel(assistances)
            ctx.json(result)
        } else {
            try {
                val kind = ctx.queryParamAsClass("kind", String::class.java)
                    .check({ c -> !c.isNullOrBlank() }, "No parameters added").get()
                val assistances = runTrx {
                    assistanceDAO.findAllByKind(kind)
                }
                val result = ResultAssistanceDTO.fromModel(assistances)
                ctx.json(result)
            } catch (e: BadRequestResponse) {
                throw BadRequestResponse(e.message!!)
            }
        }
    }
}
