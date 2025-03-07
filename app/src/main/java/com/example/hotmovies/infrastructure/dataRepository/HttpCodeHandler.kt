package com.example.hotmovies.infrastructure.dataRepository

object HttpMapper {
    fun getMessage(statusCode: Int): String = when (statusCode) {
        in 400..499 -> when (statusCode) {
            400 -> "Bad Request: The server could not understand the request due to invalid syntax."
            401 -> "Unauthorized: Access is denied due to invalid credentials."
            403 -> "Forbidden: You do not have permission to access this resource."
            404 -> "Not Found: The requested resource could not be found."
            408 -> "Request Timeout: The server timed out waiting for the request."
            else -> "Client Error: An error occurred on the client side. HTTP Status Code: $statusCode"
        }

        in 500..599 -> when (statusCode) {
            500 -> "Internal Server Error: The server encountered an unexpected condition."
            502 -> "Bad Gateway: The server received an invalid response from the upstream server."
            503 -> "Service Unavailable: The server is not ready to handle the request."
            504 -> "Gateway Timeout: The server did not receive a timely response from the upstream server."
            else -> "Server Error: An error occurred on the server side. HTTP Status Code: $statusCode"
        }

        else -> "Unexpected Error: An unknown error occurred. HTTP Status Code: $statusCode"
    }
}