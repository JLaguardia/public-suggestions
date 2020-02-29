package com.prismsoftworks.publicsuggestions.model

import java.util.*

class Suggestion private constructor(
    val id: String,
    var category: Category,
    var dateSubmitted: Date,
    var dateEdited: Date,
    var subject: String,
    var body: String,
    var hits: Hits,
    var fromContact: String = "Anonymous"){

    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var category: Category = Category.Builder().build(),
        var dateSubmitted: Date = Date(),
        var dateEdited: Date = dateSubmitted,
        var subject: String = "subject",
        var body: String = "body",
        var hits: Hits = Hits.Builder().build(),
        var fromContact: String = "Anonymous"){
            fun id(id: String) = apply { this.id = id }
            fun category(category: Category) = apply { this.category = category }
            fun dateSubmitted(dateSubmitted: Date) = apply { this.dateSubmitted = dateSubmitted }
            fun dateEdited(dateEdited: Date) = apply { this.dateEdited = dateEdited }
            fun subject(subject: String) = apply { this.subject = subject }
            fun body(body: String) = apply { this.body = body }
            fun hits(hits: Hits) = apply { this.hits = hits }
            fun fromContact(fromContact: String) = apply { this.fromContact = fromContact }
            fun build() = Suggestion(id, category, dateSubmitted, dateEdited, subject, body, hits, fromContact)
    }
}
