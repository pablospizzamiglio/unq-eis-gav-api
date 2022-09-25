import api.controllers.AssistanceController
import api.dtos.ErrorDTO
import dao.HibernateAssistanceDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.util.Header
import io.javalin.http.BadRequestResponse
import io.javalin.http.NotFoundResponse

fun main() {
    val assistanceController = AssistanceController(HibernateAssistanceDAO())

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
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
    app.exception(BadRequestResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
}
