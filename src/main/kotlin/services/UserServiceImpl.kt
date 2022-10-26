package services

import api.controllers.Validator
import dao.HibernateUserDAO
import entity.User
import java.util.*

class UserServiceImpl(private val userDAO: HibernateUserDAO) {
    private val validator: Validator = Validator()
    fun save(user: User): User {
        if (validator.containsSpecialCharacter(user.firstName)) {
            throw RuntimeException("The firstname cannot contain special characters")
        }
        return userDAO.save(user)
    }

    fun find(orderId: UUID): User {
        return userDAO.find(orderId)
    }

    fun findAll(): List<User> {
        return userDAO.findAll()
    }
}
