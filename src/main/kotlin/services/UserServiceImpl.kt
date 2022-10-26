package services

import api.dtos.UserCreateRequestDTO
import dao.HibernateUserDAO
import entity.User
import java.util.*

class UserServiceImpl(private val userDAO: HibernateUserDAO) {
    fun createUser(userCreateRequest: UserCreateRequestDTO): User {
        val user = User(
            userCreateRequest.firstName,
            userCreateRequest.lastName,
            userCreateRequest.type,
            userCreateRequest.emailAddress,
            userCreateRequest.telephoneNumber
        )
        return userDAO.save(user)
    }

    fun findByUserId(orderId: UUID): User {
        return userDAO.find(orderId)
    }
}
