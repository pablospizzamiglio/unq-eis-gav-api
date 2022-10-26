import api.controllers.AssistanceController
import api.controllers.OrderController
import api.controllers.UserController
import api.dtos.ErrorDTO
import dao.HibernateAssistanceDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.util.Header
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence


fun main() {
    val entityManagerFactory = Persistence.createEntityManagerFactory("GAV")

    val assistanceController = AssistanceController()
    val orderController = OrderController()
    val userController = UserController()

    val app = Javalin.create() {
        it.defaultContentType = "application/json"
        it.enableCorsForAllOrigins()
    }.before {
        it.header(Header.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
    }.after {
        it.attribute<EntityManager?>("entityManager")?.close()
    }.attribute("entityManagerFactory", entityManagerFactory)
    app.start(7070)

    app.routes {
        ApiBuilder.path("assistances") {
            ApiBuilder.get(assistanceController::findAll)
        }
        ApiBuilder.path("order") {
            ApiBuilder.post(orderController::generateOrder)
            ApiBuilder.put(orderController::updateOrder)
        }
        ApiBuilder.path("user") {
            ApiBuilder.post(userController::createUser)
            ApiBuilder.path("{id}") {
                ApiBuilder.get(userController::findUser)
            }
        }
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
    app.exception(BadRequestResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
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
