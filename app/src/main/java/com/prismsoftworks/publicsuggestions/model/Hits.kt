package com.prismsoftworks.publicsuggestions.model

import com.prismsoftworks.publicsuggestions.network.LikesApi
import java.util.*

class Hits private constructor(
    val id: String, //id of hit
    val entityId: String, //id of context this hit applies to
    var likes: Int,
    var dislikes: Int,
    var userHit: Hit): Observable() {

    fun setHitContext(hitContext: HitContext){
        if(this.userHit.hitContext == hitContext){
            when(this.userHit.hitContext){
                HitContext.LIKE -> this.likes--
                HitContext.DISLIKE -> this.dislikes--
            }

            this.userHit.hitContext = HitContext.NONE
        } else {
            //this should be done in backend
            when(this.userHit.hitContext){
                HitContext.LIKE -> {
                    this.likes--
                    this.dislikes++
                }

                HitContext.DISLIKE -> {
                    this.dislikes--
                    this.likes++
                }

                HitContext.NONE -> {
                    if(hitContext == HitContext.LIKE){
                        this.likes++
                    } else {
                        this.dislikes++
                    }
                }
            }

            this.userHit.hitContext = hitContext
        }

        //should this go here?
        //todo LikesApi.create().putLikeForEntity(this.userHit)

        setChanged()
        notifyObservers(this.userHit)
    }

    data class Builder(
        var id: String = UUID.randomUUID().toString(),
        var entityId: String = UUID.randomUUID().toString(),
        var likes: Int = 0,
        var dislikes: Int = 0,
        var hit: Hit = Hit.Builder().hitContext(HitContext.NONE).build()
    ) {
        fun id(id: String) = apply { this.id = id }
        fun likes(likes: Int) = apply { this.likes = likes }
        fun dislikes(dislikes: Int) = apply { this.dislikes = dislikes }
        fun entityId(entityId: String) = apply { this.entityId = entityId }
        fun hit(hitContext: HitContext) = apply { this.hit = Hit.Builder().hitContext(hitContext).build() }
        fun build() = Hits(id, entityId, likes, dislikes, hit)
    }
}