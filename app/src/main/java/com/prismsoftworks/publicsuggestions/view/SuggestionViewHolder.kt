package com.prismsoftworks.publicsuggestions.view

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prismsoftworks.publicsuggestions.R
import com.prismsoftworks.publicsuggestions.model.Hits
import com.prismsoftworks.publicsuggestions.model.Suggestion
import java.text.SimpleDateFormat
import java.util.*

class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var suggestion: Suggestion? = null

    private lateinit var imgIcon: ImageView
    private lateinit var lblCategory: TextView
    private lateinit var lblSubject: TextView
    private lateinit var btnExpand: ImageButton
    private lateinit var detailContainer: View
    private lateinit var lblDatePosted: TextView
    private lateinit var txtBody: EditText
    private lateinit var lblLike: TextView
    private lateinit var btnLike: ImageButton
    private lateinit var lblDislike: TextView
    private lateinit var btnDislike: ImageButton

    companion object{
        var editBg: Drawable? = null
    }


    fun init(suggestion: Suggestion, readOnly: Boolean = true){

        this.suggestion = suggestion
        setViews()
        lblCategory.text = suggestion.category.label
        lblSubject.text = suggestion.subject
        lblLike.text = "${suggestion.hits.likes}"
        lblDislike.text = "${suggestion.hits.dislikes}"
        lblDatePosted.text = extractDateStr()
        if(readOnly) {
            if(Objects.isNull(editBg)){
                editBg = txtBody.background
            }

            txtBody.setFocusable(false)
            txtBody.background = null
            txtBody.setOnLongClickListener { return@setOnLongClickListener true }
        } else {
            txtBody.setFocusable(true)
            txtBody.background = editBg
            txtBody.setOnLongClickListener(null)
        }

        var body = suggestion.body
        if(body.length > 100){
            body = body.substring(0, 100) + "...  Read more"
        }
        txtBody.setText(body)
        btnExpand.setOnClickListener {
            if(detailContainer.layoutParams.height == 0){
                detailContainer.layoutParams.height = -2
                detailContainer.requestLayout()
                btnExpand.setImageResource(R.drawable.ic_arrow_down_black_24dp)
            } else {
                detailContainer.layoutParams.height = 0
                detailContainer.requestLayout()
                btnExpand.setImageResource(R.drawable.ic_arrow_left_black_24dp)
            }
        }

        btnLike.setOnClickListener {
            //TODO: send put
            this.suggestion!!.hits.setHitContext(Hits.UserHit.LIKE)
//            updateHit(Hits.UserHit.LIKE)
//            this.suggestion!!.hits.likes++
//            (it as ImageButton).setImageResource(R.drawable.ic_thumb_up_black_24dp)
//            lblLike.text = "${this.suggestion!!.hits.likes}"
        }

        btnDislike.setOnClickListener {
            //TODO: send put
            this.suggestion!!.hits.setHitContext(Hits.UserHit.DISLIKE)
//            this.suggestion!!.hits.dislikes++
//            (it as ImageButton).setImageResource(R.drawable.ic_thumb_down_black_24dp)
//            lblDislike.text = "${this.suggestion!!.hits.dislikes}"
        }

    }

    private fun extractDateStr(): String {
        return "Posted on ${SimpleDateFormat("MMM, dd, yy - hh:mm:ss aa", Locale.US)
            .format(suggestion!!.dateSubmitted)}"
    }

    private fun setViews() {
        imgIcon = itemView.findViewById(R.id.imgIcon)
        lblCategory = itemView.findViewById(R.id.lblCategory)
        lblSubject = itemView.findViewById(R.id.lblSubject)
        btnExpand = itemView.findViewById(R.id.btnExpand)
        detailContainer = itemView.findViewById(R.id.detailContainer)
        lblDatePosted = itemView.findViewById(R.id.lblDatePosted)
        txtBody = itemView.findViewById(R.id.txtBody)
        lblLike = itemView.findViewById(R.id.lblLike)
        btnLike = itemView.findViewById(R.id.btnLike)
        lblDislike = itemView.findViewById(R.id.lblDislike)
        btnDislike = itemView.findViewById(R.id.btnDislike)
        refreshButtonSelection(this.suggestion!!.hits.userHit)
        this.suggestion!!.hits.addObserver { observable: Observable, any: Any ->
            refreshButtonSelection(any as Hits.UserHit)
            lblLike.text = "${this.suggestion!!.hits.likes}"
            lblDislike.text = "${this.suggestion!!.hits.dislikes}"
        }
    }

    fun refreshButtonSelection(hit: Hits.UserHit){
        when(hit){
            Hits.UserHit.LIKE -> {
                btnLike.isSelected = true
                btnDislike.isSelected = false
            }
            Hits.UserHit.DISLIKE -> {
                btnLike.isSelected = false
                btnDislike.isSelected = true
            }
            Hits.UserHit.NONE -> {
                btnLike.isSelected = false
                btnDislike.isSelected = false
            }
        }
    }
}