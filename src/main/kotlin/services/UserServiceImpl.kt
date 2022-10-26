package services

import dao.HibernateUserDAO
import entity.User
import java.util.*

class UserServiceImpl(private val userDAO: HibernateUserDAO) {

    fun save(user: User): User {
        return userDAO.save(user)
    }

    fun find(orderId: UUID): User {
        return userDAO.find(orderId)
    }
}
