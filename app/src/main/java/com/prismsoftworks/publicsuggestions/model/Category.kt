package com.prismsoftworks.publicsuggestions.model

import java.util.*

class Category private constructor(val id: String, val label: String, val description: String){

    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var label: String = "category",
        var description: String = label){
            fun id(id: String) = apply { this.id = id }
            fun label(label: String) = apply { this.label = label }
            fun description(description: String) = apply { this.description = description }
            fun build() = Category(id, label, description)
    }
}