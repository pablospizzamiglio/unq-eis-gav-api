import api.controllers.AssistanceController
import api.controllers.OrderController
import api.controllers.UserController
import api.dtos.ErrorDTO
import dao.HibernateAssistanceDAO
import dao.HibernateOrderDAO
import dao.HibernateUserDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.util.Header
import io.javalin.http.BadRequestResponse
import io.javalin.http.NotFoundResponse

fun main() {
    val assistanceController = AssistanceController(HibernateAssistanceDAO())
    val orderController = OrderController(HibernateOrderDAO(), HibernateAssistanceDAO())
    val userController = UserController(HibernateUserDAO())
    val app = Javalin.create() {
        it.defaultContentType = "application/json"
        it.enableCorsForAllOrigins()
    }.before {
        it.header(Header.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
    }
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
            ApiBuilder.get(userController::findUser)
        }
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
    app.exception(BadRequestResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
}
