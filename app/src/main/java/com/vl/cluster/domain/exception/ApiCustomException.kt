package com.vl.cluster.domain.exception

class ApiCustomException(val title: String, val description: String):
    RuntimeException("\"$title\": $description")