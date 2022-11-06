package dao

import entity.Order
import entity.OrderStatus
import javax.persistence.EntityManager

class HibernateOrderDAO(entityManager: EntityManager) : HibernateDAO<Order>(Order::class.java, entityManager) {
    fun findAllByStatus(status: List<OrderStatus>): List<Order> {
        val hql = "select p from ${entityType.simpleName} p WHERE p.status IN :status"
        val query = entityManager.createQuery(hql, entityType)
        query.setParameter("status", status)
        return query.resultList
    }
}
