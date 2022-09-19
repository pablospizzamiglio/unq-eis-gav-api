import api.controllers.AssistanceController
import api.dtos.ErrorDTO
import dao.HibernateAssistanceDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.NotFoundResponse

fun main() {
    val app = Javalin.create() {
        it.defaultContentType = "application/json"
        it.enableCorsForAllOrigins()
    }.before {
        it.header(io.javalin.core.util.Header.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
    }
    app.start(7070)

    app.before {
        it.header("Access-Control-Expose-Headers", "*")
    }

    app.routes {
        ApiBuilder.path("assistances") {
            ApiBuilder.get(AssistanceController(HibernateAssistanceDAO())::findAll)
        }
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
}
