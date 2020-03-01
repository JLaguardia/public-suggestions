package com.prismsoftworks.publicsuggestions.model

import java.util.*

class Hits private constructor(
    val id: String,
    val entityId: String,
    var likes: Int,
    var dislikes: Int,
    var userHit: UserHit): Observable() {

    fun setHitContext(userHit: UserHit){
        if(this.userHit == userHit){
            when(this.userHit){
                UserHit.LIKE -> this.likes--
                UserHit.DISLIKE -> this.dislikes--
            }

            this.userHit = UserHit.NONE
        } else {
            //this should be done in backend
            when(this.userHit){
                UserHit.LIKE -> {
                    this.likes--
                    this.dislikes++
                }

                UserHit.DISLIKE -> {
                    this.dislikes--
                    this.likes++
                }

                UserHit.NONE -> {
                    if(userHit == UserHit.LIKE){
                        this.likes++
                    } else {
                        this.dislikes++
                    }
                }
            }

            this.userHit = userHit
        }

        setChanged()
        notifyObservers(this.userHit)
    }

    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var entityId: String = UUID.randomUUID().toString(),
        var likes: Int = 0,
        var dislikes: Int = 0,
        var userHit: UserHit = UserHit.NONE
    ) {
        fun id(id: String) = apply { this.id = id }
        fun likes(likes: Int) = apply { this.likes = likes }
        fun dislikes(dislikes: Int) = apply { this.dislikes = dislikes }
        fun entityId(entityId: String) = apply { this.entityId = entityId }
        fun userHit(userHit: UserHit) = apply { this.userHit = userHit }
        fun build() = Hits(id, entityId, likes, dislikes, userHit)
    }

    enum class UserHit{
        LIKE,
        DISLIKE,
        NONE
    }
}