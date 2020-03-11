package com.prismsoftworks.publicsuggestions.model

import java.util.*

class Hit private constructor(
    val id: String,
    val hitsId: String,
    var hitContext: HitContext
){
    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var hitsId: String = UUID.randomUUID().toString(),
        var hitContext: HitContext = HitContext.NONE
    ){
        fun id(id: String) = apply { this.id = id }
        fun hitsId(hitsId: String) = apply { this.hitsId = hitsId }
        fun hitContext(hitContext: HitContext) = apply { this.hitContext = hitContext }
        fun build() = Hit(id, hitsId, hitContext)
    }
}

enum class HitContext{
    LIKE,
    DISLIKE,
    NONE
}