package com.example.a7minsworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.a7minsworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null
    private var db: HistoryDatabase? = null
    private var historyEntityList: ArrayList<HistoryEntity>? = null
    private var historyEntityAdapter: HistoryEntityAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        db = HistoryDatabase.getInstance(this.applicationContext)
        historyEntityList = getAllDatesData()
        setSupportActionBar(binding?.toolbarHistoryActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = "HISTORY"
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        setUpExerciseStatusRecyclerView(historyEntityList!!)


    }

    private fun getAllDatesData(): ArrayList<HistoryEntity> {
        var historyEntityList = ArrayList<HistoryEntity>()
        lifecycleScope.launch {
            db?.historyDao()?.fetchAllDate()?.collect {
                for (historyEntity in it) {
                    historyEntityList.add(historyEntity)
                }
            }
        }
        return historyEntityList
    }

    private fun setUpExerciseStatusRecyclerView(historyEntityList: ArrayList<HistoryEntity>) {
        if(historyEntityList.size != 0)
        {
            binding?.tvNoData?.visibility = View.VISIBLE
        }
        else
        {
            binding?.tvNoData?.visibility = View.GONE
            historyEntityAdapter = HistoryEntityAdapter(historyEntityList)
            binding!!.rvHistory.adapter = historyEntityAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}