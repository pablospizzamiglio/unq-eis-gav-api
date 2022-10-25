package services

import api.controllers.Validator
import dao.HibernateUserDAO
import entity.User
import transaction.TransactionRunner.runTrx
import java.util.*

class UserServiceImpl(
    private val userDAO: HibernateUserDAO,
    private val validator: Validator = Validator()
) {

    fun save(user: User): User {
        return runTrx {
            if (validator.containsSpecialCharacter(user.firstName)) {
                throw RuntimeException("The firstname cannot contain special characters")
            }
            userDAO.save(user)
        }
    }

    fun find(orderId: UUID): User {
        return runTrx {
            userDAO.find(orderId)
        }
    }

    fun findAll(): List<User> {
        return runTrx {
            userDAO.findAll()
        }
    }
}
