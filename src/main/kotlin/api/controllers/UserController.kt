package api.controllers

import api.dtos.UserDTO
import entity.User
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import services.UserServiceImpl
import java.util.*

class UserController(private val userService: UserServiceImpl) {

    fun createUser(ctx: Context) {
        try {
            val newUser = ctx.bodyValidator<UserDTO>()
                .check({ obj -> !obj.firstName.isNullOrBlank() }, "The first name was not loaded")
                .check({ obj -> !obj.lastName.isNullOrBlank() }, "The last name was not loaded")
                .check({ obj -> !obj.type.isNullOrBlank() }, "The type was not loaded")
                .check({ obj -> !obj.emailAddress.isNullOrBlank() }, "The email not loaded")
                .check({ obj -> !obj.telephoneNumber.isNullOrBlank() }, "The telephone number was not loaded").get()
            val user = userService.save(
                User(
                    newUser.firstName,
                    newUser.lastName,
                    newUser.type,
                    newUser.emailAddress,
                    newUser.telephoneNumber
                )
            )
            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

    fun findUser(ctx: Context) {
        try {
            val id = ctx.pathParam("id")
            val userId = UUID.fromString(id)
            val user = userService.find(userId)
            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
