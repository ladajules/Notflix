package com.ladajules.notflix.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ladajules.notflix.R

class SearchActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView
    private lateinit var tvCancel: TextView
    private lateinit var rvSearchResults: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        etSearch = findViewById(R.id.etSearch)
        ivClearSearch = findViewById(R.id.ivClearSearch)
        tvCancel = findViewById(R.id.tvCancel)
        rvSearchResults = findViewById(R.id.rvSearchResults)
    }

    private fun setupListeners() {
        ivBack.setOnClickListener {
            finish()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ivClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        ivClearSearch.setOnClickListener {
            etSearch.text.clear()
            etSearch.requestFocus()
        }
        
        tvCancel.setOnClickListener {
            finish()
        }
    }
}
