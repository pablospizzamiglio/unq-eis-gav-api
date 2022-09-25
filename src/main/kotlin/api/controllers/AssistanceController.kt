package api.controllers

import api.dtos.ResultAssistanceDTO
import dao.HibernateAssistanceDAO
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class AssistanceController(private val assistanceDAO: HibernateAssistanceDAO) {
    fun findAll(ctx: Context) {
        val kind = ctx.queryParam("kind")
        if (kind.isNullOrBlank()) {
            val assistances = runTrx {
                assistanceDAO.findAll()
            }
            val result = ResultAssistanceDTO.fromModel(assistances)
            ctx.json(result)
        } else {
            val assistances = runTrx {
                assistanceDAO.findAllByKind(kind)
            }
            val result = ResultAssistanceDTO.fromModel(assistances)
            ctx.json(result)
        }
    }
}
