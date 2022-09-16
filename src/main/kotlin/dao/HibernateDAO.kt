package dao

import transaction.HibernateTransaction
import java.util.UUID

open class HibernateDAO<T>(private val entityType: Class<T>) {
    open fun save(entity: T): T {
        val session = HibernateTransaction.currentSession
        session.save(entity)
        return entity
    }

    open fun update(entity: T) {
        val session = HibernateTransaction.currentSession
        session.update(entity)
    }

    open fun find(id: UUID): T {
        val session = HibernateTransaction.currentSession
        return session.get(entityType, id) ?: throw RuntimeException("The entity $entityType with id $id does not exist")
    }

    open fun findAll(): List<T> {
        val session = HibernateTransaction.currentSession

        val hql = "select p from ${entityType.simpleName} p"
        val query = session.createQuery(hql, entityType)

        return query.resultList
    }

    open fun count(): Int {
        val session = HibernateTransaction.currentSession

        val hql = "select count (p) from ${entityType.simpleName} p"
        val query = session.createQuery(hql)

        return (query.uniqueResult() as Long).toInt()
    }
}
