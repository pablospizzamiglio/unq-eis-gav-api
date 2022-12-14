import api.controllers.AssistanceController
import api.controllers.OrderController
import api.controllers.UserController
import api.dtos.ErrorDTO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.Header
import io.javalin.core.validation.ValidationException
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HttpCode
import io.javalin.http.NotFoundResponse
import java.util.Properties
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence


fun main() {
    val dbHost = System.getenv("DB_HOST")
    val dbConnectionURL = "jdbc:mysql://$dbHost:3306/gav?createDatabaseIfNotExist=true&serverTimezone=UTC"

    val properties = Properties()
    properties.setProperty("hibernate.connection.url", dbConnectionURL)

    val entityManagerFactory = Persistence.createEntityManagerFactory("GAV", properties)

    val assistanceController = AssistanceController()
    val orderController = OrderController()
    val userController = UserController()

    val app = Javalin.create {
        it.defaultContentType = "application/json"
        it.enableCorsForAllOrigins()
    }.before {
        it.header(Header.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
    }.after {
        it.attribute<EntityManager?>("entityManager")?.close()
    }.attribute("entityManagerFactory", entityManagerFactory)
    app.start(7070)

    app.routes {
        path("assistance") {
            get(assistanceController::findAll)
        }
        path("score"){
            put(orderController::updateScore)
        }
        path("orders") {
            get(orderController::findAll)
            post(orderController::createOrder)
            put(orderController::updateOrder)
            path("{id}") {
                get(orderController::findOrder)
            }
        }
        path("users") {
            post(userController::createUser)
            path("{id}") {
                get(userController::findUser)
            }
        }
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
    app.exception(BadRequestResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
    app.exception(ValidationException::class.java) { e, ctx ->
        ctx.status(HttpCode.BAD_REQUEST)
        if (e.errors.values.any { errors -> errors.any { it.message == "DESERIALIZATION_FAILED" } }) {
            ctx.json(ErrorDTO("Malformed request"))
        } else if (e.errors.keys.contains("REQUEST_BODY")) {
            ctx.json(ErrorDTO.fromErrors(e.errors["REQUEST_BODY"]!!))
        } else {
            ctx.json(e.errors)
        }
    }
}

val Context.entityManager: EntityManager
    get() {
        if (this.attribute<EntityManager>("entityManager") == null) {
            this.attribute(
                "entityManager",
                this.appAttribute<EntityManagerFactory>("entityManagerFactory").createEntityManager()
            )
        }
        return this.attribute("entityManager")!!
    }
