package sources

import io.github.tabilzad.ktor.annotations.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import sources.requests.SimpleRequest

class MyGenericRespondsType<T, S>(
    val amounts: List<MyAmount<T>>,
    val currency: MyCurrency<S>
)

class MyAmount<T>(val amount: T)
class MyCurrency<T>(val curr: T)

@GenerateOpenApi
fun Application.multipleResponds() {
    routing {
        route("/v1") {
            post("/multipleResponseWithSameClassButDifferentGenerics") {
                // This is a success response
                // next line of the description
                responds<MyGenericRespondsType<String, Int>>(HttpStatusCode.OK)
                /* This is a 400 bad request
                  next line after 400
                 */
                responds<MyGenericRespondsType<Boolean, List<Boolean>>>(HttpStatusCode.BadRequest)

                // should be ignored
                responds<String>(HttpStatusCode.InternalServerError, "This is a custom error message")
                // call.receive<SimpleRequest>() ignore comment
            }
        }
    }
}
