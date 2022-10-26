package dao

import entity.Order
import javax.persistence.EntityManager

class HibernateOrderDAO(entityManager: EntityManager) : HibernateDAO<Order>(Order::class.java, entityManager)
