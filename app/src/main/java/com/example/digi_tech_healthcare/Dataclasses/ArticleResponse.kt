package com.example.digi_tech_healthcare.Dataclasses

data class ArticleResponse(
    var articles: List<Articles>? = null,
    var exception: Exception? = null
)