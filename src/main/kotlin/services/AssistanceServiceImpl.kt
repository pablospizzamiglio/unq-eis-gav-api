package services

import dao.HibernateAssistanceDAO
import entity.Assistance
import java.util.*

class AssistanceServiceImpl(private val assistanceDAO: HibernateAssistanceDAO) {

    fun findAll(): List<Assistance> {
        return assistanceDAO.findAll()
    }

    fun findAllByKind(kind: String): List<Assistance> {
        return assistanceDAO.findAllByKind(kind)
    }

    fun find(assistanceId: UUID): Assistance {
        return assistanceDAO.find(assistanceId)
    }
}
