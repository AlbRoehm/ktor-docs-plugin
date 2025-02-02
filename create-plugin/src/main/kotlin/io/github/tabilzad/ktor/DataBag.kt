package io.github.tabilzad.ktor

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.tabilzad.ktor.output.OpenApiSpec

interface OpenApiSpecParam {
    val name: String
    @Suppress("VariableNaming")
    val `in`: String
    val required: Boolean
    val description: String?
}

internal interface ParamSpec {
    val name: String
    val description: String?
}

internal data class PathParamSpec(
    override val name: String,
    override val description: String? = null,
) : ParamSpec

internal data class QueryParamSpec(
    override val name: String,
    override val description: String? = null,
    val isRequired: Boolean = false
) : ParamSpec

internal data class HeaderParamSpec(
    override val name: String,
    override val description: String? = null,
    val isRequired: Boolean = false
) : ParamSpec

internal data class KtorRouteSpec(
    val path: String,
    val parameters: List<ParamSpec>?,
    val method: String,
    val body: OpenApiSpec.ObjectType,
    val summary: String?,
    val description: String?,
    val operationId: String?,
    val tags: Set<String>?,
    val responses: Map<String, OpenApiSpec.ResponseDetails>?
)

sealed class KtorElement {
    abstract var path: String?
    abstract var tags: Set<String>?

    abstract fun newInstance(tags: Set<String>?): KtorElement
}

enum class ExpType(val labels: List<String>) {
    ROUTE(listOf("route")),
    METHOD(listOf("get", "post", "put", "patch", "delete")),
    RECEIVE(listOf("receive"))
}

internal data class EndPoint(
    override var path: String?,
    val method: String = "",
    var body: OpenApiSpec.ObjectType? = null,
    var parameters: Set<ParamSpec>? = null,
    var description: String? = null,
    var operationId: String? = null,
    var summary: String? = null,
    override var tags: Set<String>? = null,
    var responses: Map<String, OpenApiSpec.ResponseDetails>? = null
) : KtorElement() {
    override fun newInstance(tags: Set<String>?): EndPoint {
        return copy(tags = tags)
    }
}

data class DocRoute(
    override var path: String? = "/",
    val children: MutableList<KtorElement> = mutableListOf(),
    override var tags: Set<String>? = null
) : KtorElement() {
    override fun newInstance(tags: Set<String>?): DocRoute {
        return copy(tags = tags)
    }
}

enum class ContentType {
    @JsonProperty("application/json")
    APPLICATION_JSON,

    @JsonProperty("text/plain")
    TEXT_PLAIN
}
