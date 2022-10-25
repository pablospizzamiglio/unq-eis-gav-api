package services

import dao.HibernateAssistanceDAO
import entity.Assistance
import transaction.TransactionRunner.runTrx
import java.util.*

class AssistanceServiceImpl(
    private val assistanceDAO: HibernateAssistanceDAO
) {

    fun findAll(): List<Assistance> {
        return runTrx {
            assistanceDAO.findAll()
        }
    }

    fun findAllByKind(kind: String): List<Assistance> {
        return runTrx {
            assistanceDAO.findAllByKind(kind)
        }
    }

    fun find(assistanceId: UUID): Assistance {
        return runTrx {
            assistanceDAO.find(assistanceId)
        }
    }
}
