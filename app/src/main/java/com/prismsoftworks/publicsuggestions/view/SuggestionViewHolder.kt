package com.prismsoftworks.publicsuggestions.view

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.recyclerview.widget.RecyclerView
import com.prismsoftworks.publicsuggestions.R
import com.prismsoftworks.publicsuggestions.model.Hit
import com.prismsoftworks.publicsuggestions.model.HitContext
import com.prismsoftworks.publicsuggestions.model.Suggestion
import kotlinx.android.synthetic.main.suggestion_itemview.view.*
import java.text.SimpleDateFormat
import java.util.*


class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var suggestion: Suggestion? = null

    companion object{
        var editBg: Drawable? = null
    }


    fun init(suggestion: Suggestion, readOnly: Boolean = true){
        this.suggestion = suggestion
        with(itemView){
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
                if(detailContainer.visibility == View.GONE){
                    expandView(detailContainer)
                    btnExpand.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                } else {
                    shrinkView(detailContainer)
                    btnExpand.setImageResource(R.drawable.ic_arrow_down_black_24dp)
                }
            }

            btnLike.setOnClickListener {
                suggestion.hits.setHitContext(HitContext.LIKE)
            }

            btnDislike.setOnClickListener {
                suggestion.hits.setHitContext(HitContext.DISLIKE)
            }
        }

    }

    private fun shrinkView(v: View) {
        v.startAnimation(ShrinkAnim(v))
    }

    private fun expandView(v: View) {
        v.visibility = View.VISIBLE
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (v.parent as View).width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.measuredHeight
        v.startAnimation(ExpandAnim(v, targetHeight))
    }

    private class ShrinkAnim(val v: View): Animation(){
        var initialHeight = v.measuredHeight
        init {
            duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()

        }
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    private class ExpandAnim(val v: View, val targetHeight: Int): Animation(){
        init {
            duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()

        }
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            v.layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
            else (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    private fun extractDateStr(): String {
        return "Posted on ${SimpleDateFormat("MMM, dd, yy - hh:mm:ss aa", Locale.US)
            .format(suggestion!!.dateSubmitted)}"
    }

    private fun setViews() {
        refreshButtonSelection(this.suggestion!!.hits.userHit.hitContext)
        this.suggestion!!.hits.addObserver { _: Observable, hit: Any ->
            refreshButtonSelection((hit as Hit).hitContext)
            with(itemView) {
                lblLike.text = "${suggestion!!.hits.likes}"
                lblDislike.text = "${suggestion!!.hits.dislikes}"
            }
        }
    }

    private fun refreshButtonSelection(hit: HitContext){
        with(itemView) {
            when (hit) {
                HitContext.LIKE -> {
                    btnLike.isSelected = true
                    btnDislike.isSelected = false
                }
                HitContext.DISLIKE -> {
                    btnLike.isSelected = false
                    btnDislike.isSelected = true
                }
                HitContext.NONE -> {
                    btnLike.isSelected = false
                    btnDislike.isSelected = false
                }
            }
        }
    }
}