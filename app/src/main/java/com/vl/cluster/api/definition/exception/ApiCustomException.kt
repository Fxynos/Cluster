package com.vl.cluster.api.definition.exception

class ApiCustomException(val title: String, val description: String):
    RuntimeException("\"$title\": $description")