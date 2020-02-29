package com.prismsoftworks.publicsuggestions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.prismsoftworks.publicsuggestions.adapter.SuggestionAdapter
import com.prismsoftworks.publicsuggestions.model.Category
import com.prismsoftworks.publicsuggestions.model.Hits
import com.prismsoftworks.publicsuggestions.model.Suggestion
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lone_recycler.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        LayoutInflater.from(this).inflate(R.layout.lone_recycler, contentLayout)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = SuggestionAdapter().setItems(generateItems()).setReadOnly(true)
    }

    private fun generateItems(): List<Suggestion> {
        val list = ArrayList<Suggestion>()
        list.add(Suggestion.Builder()
            .id("1")
            .category(genCategory())
            .dateSubmitted(Date())
            .subject("Mr. Jones")
            .body("Mr jones is a asshole yo")
            .hits(generateHits())
            .build())
        list.add(Suggestion.Builder()
            .id("2")
            .category(genCategory()).dateSubmitted(Date()).dateEdited(Date())
            .subject("Student Parking")
            .body("I have an issue with the way the campus runs the parking lot. I think that " +
                    "students with permits should get priority parking and those without should " +
                    "not be allowed.")
            .hits(generateHits())
            .build())
        list.add(Suggestion.Builder()
            .id("3")
            .category(genCategory())
            .dateSubmitted(Date())
            .subject("Jessica")
            .body("Wtf? Smoking on campus allowed?! Get her tf outta here!!!")
            .hits(generateHits())
            .build())
        list.add(Suggestion.Builder()
            .id("4")
            .category(genCategory())
            .subject("Bathrooms")
            .body("Hire more janitors. These kids dont flush!")
            .hits(generateHits())
            .build())
        list.add(Suggestion.Builder()
            .id("5")
            .category(genCategory())
            .subject("Coaches")
            .body("why do the coaches sux")
            .hits(generateHits())
            .build())
        return list
    }

    private fun generateHits(): Hits {
        val likes = Math.random() * 100
        val dislikes = Math.random() * 100
        return Hits.Builder().likes(likes.toInt()).dislikes(dislikes.toInt()).build()
    }

    private fun genCategory(): Category {
        when(((Math.random() * 4) + 1).toInt()){
            1 -> return Category.Builder().id("1").label("FACULTY").description("Faculty of the establishment").build()
            2 -> return Category.Builder().id("2").label("FACILITY").description("Faculty of the establishment").build()
            3 -> return Category.Builder().id("3").label("STDT GOV").description("Faculty of the establishment").build()
            4 -> return Category.Builder().id("4").label("SERVICES").description("Faculty of the establishment").build()
            5 -> return Category.Builder().id("5").label("FACULTY").description("Faculty of the establishment").build()
        }

        return Category.Builder().build()
    }


}
