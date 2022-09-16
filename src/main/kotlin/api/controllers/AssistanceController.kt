package api.controllers

import api.dtos.controllers.dtos.ResultAssistencesDTO
import dao.HibernateAssistanceDAO
import io.javalin.http.Context
import transaction.TransactionRunner

data class AssistanceController(val hibernateAssistanceDAO: HibernateAssistanceDAO) {
    fun findAll(ctx: Context) {
        val assistances = TransactionRunner.runTrx {
            hibernateAssistanceDAO.findAll()
        }
        val result = ResultAssistencesDTO.fromModel(assistances)
        ctx.json(result)
    }
}