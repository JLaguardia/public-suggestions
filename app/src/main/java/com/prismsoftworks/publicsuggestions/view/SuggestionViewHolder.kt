package com.prismsoftworks.publicsuggestions.view

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.recyclerview.widget.RecyclerView
import com.prismsoftworks.publicsuggestions.R
import com.prismsoftworks.publicsuggestions.databinding.SuggestionItemviewBinding
import com.prismsoftworks.publicsuggestions.model.Hit
import com.prismsoftworks.publicsuggestions.model.HitContext
import com.prismsoftworks.publicsuggestions.model.Suggestion
import java.text.SimpleDateFormat
import java.util.*


class SuggestionViewHolder(
    private val binding: SuggestionItemviewBinding,
    private val listener: (body: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var suggestion: Suggestion? = null

    companion object {
        var editBg: Drawable? = null
    }

    fun init(suggestion: Suggestion, readOnly: Boolean = true) {
        this.suggestion = suggestion
        with(itemView) {
            setViews()
            binding.lblCategory.text = suggestion.category.label
            binding.lblSubject.text = suggestion.subject
            binding.lblLike.text = "${suggestion.hits.likes}"
            binding.lblDislike.text = "${suggestion.hits.dislikes}"
            binding.lblDatePosted.text = extractDateStr()
            if (readOnly) {
                if (Objects.isNull(editBg)) {
                    editBg = binding.txtBody.background
                }

                binding.txtBody.isFocusable = false
                binding.txtBody.background = null
                binding.txtBody.setOnLongClickListener { return@setOnLongClickListener true }
            } else {
                binding.txtBody.isFocusable = true
                binding.txtBody.background = editBg
                binding.txtBody.setOnLongClickListener(null)
            }

            var body = suggestion.body
            if (body.length > 100) {
                body = body.substring(0, 120) + context.getString(R.string.read_more)
                binding.txtBody.setOnClickListener { listener(suggestion.body) }
            }
            binding.txtBody.setText(body)
            binding.btnExpand.setOnClickListener {
                if (binding.detailContainer.visibility == View.GONE) {
                    expandView(binding.detailContainer)
                    binding.btnExpand.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                } else {
                    shrinkView(binding.detailContainer)
                    binding.btnExpand.setImageResource(R.drawable.ic_arrow_down_black_24dp)
                }
            }

            binding.btnLike.setOnClickListener {
                suggestion.hits.setHitContext(HitContext.LIKE)
            }

            binding.btnDislike.setOnClickListener {
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

    private class ShrinkAnim(val v: View) : Animation() {
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

    private class ExpandAnim(val v: View, val targetHeight: Int) : Animation() {
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
        return "Posted on ${
            SimpleDateFormat("MMM, dd, yy - hh:mm:ss aa", Locale.US)
                .format(suggestion!!.dateSubmitted)
        }"
    }

    private fun setViews() {
        refreshButtonSelection(this.suggestion!!.hits.userHit.hitContext)
        this.suggestion!!.hits.addObserver { _: Observable, hit: Any ->
            refreshButtonSelection((hit as Hit).hitContext)
            with(itemView) {
                binding.lblLike.text = "${suggestion!!.hits.likes}"
                binding.lblDislike.text = "${suggestion!!.hits.dislikes}"
            }
        }
    }

    private fun refreshButtonSelection(hit: HitContext) {
        with(itemView) {
            when (hit) {
                HitContext.LIKE -> {
                    binding.btnLike.isSelected = true
                    binding.btnDislike.isSelected = false
                }
                HitContext.DISLIKE -> {
                    binding.btnLike.isSelected = false
                    binding.btnDislike.isSelected = true
                }
                HitContext.NONE -> {
                    binding.btnLike.isSelected = false
                    binding.btnDislike.isSelected = false
                }
            }
        }
    }
}