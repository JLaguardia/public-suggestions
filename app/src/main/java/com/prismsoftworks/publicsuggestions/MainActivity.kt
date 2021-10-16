package com.prismsoftworks.publicsuggestions

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prismsoftworks.publicsuggestions.adapter.SuggestionAdapter
import com.prismsoftworks.publicsuggestions.databinding.ActivityMainBinding
import com.prismsoftworks.publicsuggestions.model.Category
import com.prismsoftworks.publicsuggestions.model.HitContext
import com.prismsoftworks.publicsuggestions.model.Hits
import com.prismsoftworks.publicsuggestions.model.Suggestion
import com.prismsoftworks.publicsuggestions.view.SuggestionViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import org.kodein.di.android.*

class MainActivity : AppCompatActivity(), KodeinAware, CoroutineScope {

    private lateinit var binding: ActivityMainBinding
    override val kodein: Kodein by kodein()
    private val job = Job() + Dispatchers.Main
    override val coroutineContext: CoroutineContext = job
    val navCtrl by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private val onReadMore: (body: String) -> Unit = { body ->
        with(binding.popupContainer) {
            if (!isVisible) {
                val restOfText = TextView(this@MainActivity).apply {
                    text = body
                    setTextColor(ColorStateList.valueOf(Color.WHITE))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f)
                }
                addView(restOfText)
                isVisible = true
                restOfText.updateLayoutParams<FrameLayout.LayoutParams> {
                    width = -2
                    height = -2
                    gravity = Gravity.CENTER
                }
            }
        }

        val closebtn = Button(this).apply {
            text = "close"
            setOnClickListener { binding.root.removeView(this.parent as FrameLayout) }
        }
    }

    private fun initialize() {
        LayoutInflater.from(this).inflate(R.layout.lone_recycler, binding.contentLayout)
        with(binding.contentLayout.findViewById(R.id.recyclerView) as RecyclerView) {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = SuggestionAdapter(onReadMore).setItems(generateItems()).setReadOnly(true)
//        (recyclerView.adapter as SuggestionAdapter).notifyDataSetChanged()
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(this@MainActivity, R.anim.la_falldown)
        }
        binding.popupContainer.setOnClickListener {
            (it as FrameLayout).removeAllViews()
            it.isVisible = false
        }
    }

    class Swiper(private val adapter: SuggestionAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onSwipe(viewHolder as SuggestionViewHolder, DIRECTION.fromInt(direction))
        }

        enum class DIRECTION {
            UP,
            DOWN,
            LEFT,
            RIGHT,
            UNKNOWN;

            companion object {
                fun fromInt(value: Int) =
                    when (value) {
                        1 -> UP
                        1 shl 1 -> DOWN
                        1 shl 2 -> LEFT
                        1 shl 3 -> RIGHT
                        else -> UNKNOWN
                    }

            }
        }

    }

    private fun generateItems(): List<Suggestion> {
        val list = ArrayList<Suggestion>()
        list.add(
            Suggestion.Builder()
                .id("1")
                .category(genCategory())
                .dateSubmitted(Date())
                .subject("Mr. Jones")
                .body("Mr jones is a asshole yo")
                .hits(generateHits())
                .build()
        )
        list.add(
            Suggestion.Builder()
                .id("2")
                .category(genCategory()).dateSubmitted(Date()).dateEdited(Date())
                .subject("Student Parking")
                .body(
                    "I have an issue with the way the campus runs the parking lot. I think that " +
                            "students with permits should get priority parking and those without should " +
                            "not be allowed. I mean come on, why force students to pay for parking permits " +
                            "and not enforce it by towing unauthorized vehicles?"
                )
                .hits(generateHits())
                .build()
        )
        list.add(
            Suggestion.Builder()
                .id("3")
                .category(genCategory())
                .dateSubmitted(Date())
                .subject("Jessica")
                .body("Wtf? Smoking on campus allowed?! Get her tf outta here!!!")
                .hits(generateHits())
                .build()
        )
        list.add(
            Suggestion.Builder()
                .id("4")
                .category(genCategory())
                .subject("Bathrooms")
                .body("Hire more janitors. These kids dont flush!")
                .hits(generateHits())
                .build()
        )
        list.add(
            Suggestion.Builder()
                .id("5")
                .category(genCategory())
                .subject("Coaches")
                .body("why do the coaches sux")
                .hits(generateHits())
                .build()
        )
        return list
    }

    private fun generateHits(): Hits {
        val likes = Math.random() * 100
        val dislikes = Math.random() * 100
        val hitSeed = Random.nextInt(1, 3)
        var hit: HitContext = HitContext.NONE
        when (hitSeed) {
            1 -> hit = HitContext.LIKE
            2 -> hit = HitContext.DISLIKE
            3 -> hit = HitContext.NONE
        }
        return Hits.Builder().likes(likes.toInt()).dislikes(dislikes.toInt()).hit(hit).build()
    }

    private fun genCategory(): Category {
        when (((Math.random() * 4) + 1).toInt()) {
            1 -> return Category.Builder().id("1").label("FACULTY")
                .description("Faculty of the establishment").build()
            2 -> return Category.Builder().id("2").label("FACILITY")
                .description("Faculty of the establishment").build()
            3 -> return Category.Builder().id("3").label("STDT GOV")
                .description("Faculty of the establishment").build()
            4 -> return Category.Builder().id("4").label("SERVICES")
                .description("Faculty of the establishment").build()
            5 -> return Category.Builder().id("5").label("FACULTY")
                .description("Faculty of the establishment").build()
        }

        return Category.Builder().build()
    }
}
