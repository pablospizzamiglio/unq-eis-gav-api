package services

import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import entity.Assistance

class AssistanceServiceImpl(
    private val assistanceDAO: HibernateAssistanceDAO,
    private val orderDAO: HibernateOrderDAO
) {
    fun findAll(): List<Pair<Assistance, Int>> {
        val assistances = assistanceDAO.findAll()
        val orders = orderDAO.findAll()

        var scores = assistances.map {
            val orders = orders.filter { order -> order.assistance.id!! == it.id }
            orders.sumOf { it.score / orders.size }
        }
        return assistances.zip(scores)
        //assistanceDAO.findAll()
    }

    fun findAllByKind(kind: String): List<Pair<Assistance, Int>> {
        val assistances =  assistanceDAO.findAllByKind(kind)
        val orders = orderDAO.findAll()

        var scores = assistances.map {
            val orders = orders.filter { order -> order.assistance.id!! == it.id }
            orders.sumOf { it.score / orders.size }
        }
        return assistances.zip(scores)
    }
}
