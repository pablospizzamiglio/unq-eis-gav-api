import api.controllers.AssistanceController
import api.dtos.ErrorDTO
import dao.HibernateAssistanceDAO
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.http.NotFoundResponse

fun main() {
    val app = Javalin.create().start(7070)
    app.routes {
        ApiBuilder.path("assistances") {
            ApiBuilder.get(AssistanceController(HibernateAssistanceDAO())::findAll)
        }
    }

    app.exception(NotFoundResponse::class.java) { e, ctx ->
        ctx.status(e.status).json(ErrorDTO(e.message!!))
    }
}
