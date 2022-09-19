package api.controllers

import api.dtos.ResultAssistanceDTO
import dao.HibernateAssistanceDAO
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class AssistanceController(private val assistanceDAO: HibernateAssistanceDAO) {
    fun findAll(ctx: Context) {
        val assistances = runTrx {
            assistanceDAO.findAll()
        }
        val result = ResultAssistanceDTO.fromModel(assistances)
        ctx.json(result)
    }
}
