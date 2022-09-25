package dao

import entity.Assistance
import transaction.HibernateTransaction

class HibernateAssistanceDAO : HibernateDAO<Assistance>(Assistance::class.java) {
    fun findAllByKind(kind: String): List<Assistance> {
        val session = HibernateTransaction.currentSession
        val hql = "select p from Assistance p WHERE p.kind = '$kind'"
        val query = session.createQuery(hql, Assistance::class.java)
        return query.list()
    }
}
