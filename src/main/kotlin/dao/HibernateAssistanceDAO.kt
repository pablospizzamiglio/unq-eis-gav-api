package dao

import entity.Assistance
import javax.persistence.EntityManager

class HibernateAssistanceDAO(entityManager: EntityManager) : HibernateDAO<Assistance>(Assistance::class.java, entityManager) {
    fun findAllByKind(kind: String): List<Assistance> {
        val hql = "select p from Assistance p WHERE p.kind = '$kind'"
        val query = entityManager.createQuery(hql, Assistance::class.java)
        return query.resultList
    }
}
