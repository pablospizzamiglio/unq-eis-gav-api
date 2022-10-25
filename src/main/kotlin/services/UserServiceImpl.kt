package services

import dao.HibernateUserDAO
import entity.User
import transaction.TransactionRunner.runTrx
import java.util.*

class UserServiceImpl(
    private val userDAO: HibernateUserDAO
) {

    fun save(user: User): User {
        return runTrx {
            userDAO.save(user)
        }
    }

    fun find(orderId: UUID): User {
        return runTrx {
            userDAO.find(orderId)
        }
    }
}
