package com.prismsoftworks.publicsuggestions.model

import java.util.*

class Hits private constructor(
    val id: String,
    var likes: Int,
    var dislikes: Int,
    val entityId: String){

    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var likes: Int = 0,
        var dislikes: Int = 0,
        var entityId: String = UUID.randomUUID().toString()
    ){
        fun id(id: String) = apply { this.id = id }
        fun likes(likes: Int) = apply { this.likes = likes }
        fun dislikes(dislikes: Int) = apply { this.dislikes = dislikes }
        fun entityId(entityId: String) = apply { this.entityId = entityId }
        fun build() = Hits(id, likes, dislikes, entityId)
    }
}