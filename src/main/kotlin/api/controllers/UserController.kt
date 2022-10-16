package api.controllers

import api.dtos.UserDTO
import api.dtos.UserIdDTO
import dao.HibernateUserDAO
import entity.User
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import transaction.TransactionRunner.runTrx

class UserController(private val userDAO: HibernateUserDAO) {

    fun createUser(ctx: Context) {
        try {
            val dates = ctx.bodyValidator<UserDTO>()
                .check({ obj -> !obj.firstName.isNullOrBlank() }, "The first name was not loaded")
                .check({ obj -> !obj.lastName.isNullOrBlank() }, "The last name was not loaded")
                .check({ obj -> !obj.type.isNullOrBlank() }, "The type was not loaded")
                .check({ obj -> !obj.emailAddress.isNullOrBlank() }, "The email not loaded")
                .check({ obj -> !obj.telephoneNumber.isNullOrBlank() }, "The telephone number was not loaded").get()
            val user = runTrx {
                userDAO.save(
                    User(
                        dates.firstName,
                        dates.lastName,
                        dates.type,
                        dates.emailAddress,
                        dates.telephoneNumber
                    )
                )
            }
            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

    fun findUser(ctx: Context) {
        try {
            val dates = ctx.bodyValidator<UserIdDTO>()
                .check({ obj -> !obj.toString().isNullOrBlank() }, "The id user was not loaded").get()
            val user = runTrx {
                userDAO.find(dates.id)
            }
            ctx.json(user)
        } catch (e: java.lang.RuntimeException) {
            throw BadRequestResponse(e.message!!)
        }
    }

}
