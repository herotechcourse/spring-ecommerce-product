package ecommerce.auth.helper

class RequestLine(line: String) {
    val method: String
    val url: String
    val version: String

    init {
        val parts = line.trim().split(" ").map { it.trim() }.filter { it.isNotEmpty() }
        require(parts.size == 3) { "Expected 3 parts in request line, got ${parts.size}: $line" }

        method = parts[0]
        url = parts[1]
        version = parts[2]
    }
}
