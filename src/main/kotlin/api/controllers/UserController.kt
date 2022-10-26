package api.controllers

import api.dtos.UserDTO
import dao.HibernateUserDAO
import entity.User
import entityManager
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.UserServiceImpl
import java.util.*

class UserController {

    fun createUser(ctx: Context) {
        try {
            val newUser = ctx.bodyValidator<UserDTO>()
                .check({ obj -> obj.firstName.isNotBlank() }, "The first name was not loaded")
                .check({ obj -> obj.lastName.isNotBlank() }, "The last name was not loaded")
                .check({ obj -> obj.type.isNotBlank() }, "The type was not loaded")
                .check({ obj -> obj.emailAddress.isNotBlank() }, "The email not loaded")
                .check({ obj -> obj.telephoneNumber.isNotBlank() }, "The telephone number was not loaded").get()

            val userDAO = HibernateUserDAO(ctx.entityManager)
            val userService = UserServiceImpl(userDAO)

            ctx.entityManager.transaction.begin()

            val user = userService.save(
                User(
                    newUser.firstName,
                    newUser.lastName,
                    newUser.type,
                    newUser.emailAddress,
                    newUser.telephoneNumber
                )
            )

            ctx.entityManager.transaction.commit()

            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

    fun findUser(ctx: Context) {
        try {
            val id = ctx.pathParam("id")
            val userId = UUID.fromString(id)

            val userDAO = HibernateUserDAO(ctx.entityManager)
            val userService = UserServiceImpl(userDAO)

            val user = userService.find(userId)
            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
