package api.controllers

import api.dtos.UserCreateRequestDTO
import dao.HibernateUserDAO
import entityManager
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.UserServiceImpl
import java.util.*

class UserController {

    fun createUser(ctx: Context) {
        try {
            val userCreateRequest = ctx.bodyValidator<UserCreateRequestDTO>()
                .check({ obj -> obj.firstName.isNotBlank() }, "First Name can not be empty")
                .check({ obj -> obj.lastName.isNotBlank() }, "Last Name can not be empty")
                .check({ obj -> obj.type.isNotBlank() }, "Type can not be empty")
                .check({ obj -> obj.emailAddress.isNotBlank() }, "Email can not be empty")
                .check({ obj -> obj.telephoneNumber.isNotBlank() }, "Telephone Number can not be empty").get()

            val userDAO = HibernateUserDAO(ctx.entityManager)
            val userService = UserServiceImpl(userDAO)

            ctx.entityManager.transaction.begin()
            val user = userService.createUser(userCreateRequest)
            ctx.entityManager.transaction.commit()

            ctx.json(user)
        } catch (e: RuntimeException) {
            ctx.entityManager.transaction.rollback()
            throw BadRequestResponse(e.message!!)
        }
    }

    fun findUser(ctx: Context) {
        try {
            val id = ctx.pathParam("id")
            val userId = UUID.fromString(id)

            val userDAO = HibernateUserDAO(ctx.entityManager)
            val userService = UserServiceImpl(userDAO)

            val user = userService.findByUserId(userId)

            ctx.json(user)
        } catch (e: RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
