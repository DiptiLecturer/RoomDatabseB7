package org.freedu.roomdatabaseb7

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.freedu.roomdatabaseb7.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var employeeAdapter: EmployeeAdapter
    private lateinit var database: EmployeeDatabase
    private lateinit var dao: EmployeeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Initialize DB
        database = EmployeeDatabase.getDatabase(this)
        dao = database.employeeDao()

        // 2. Setup UI
        setupRecyclerView()
        setupSearch()

        // 3. Load Initial Data
        loadEmployees()

        binding.ivAddEmployee.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        employeeAdapter = EmployeeAdapter(emptyList()) { employee ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("employee_id", employee.id)
            startActivity(intent)
        }

        binding.rvEmployees.apply {
            layoutManager = LinearLayoutManager(this@HomeScreenActivity)
            adapter = employeeAdapter
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    performSearch(newText)
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        // SQL LIKE syntax: %text% matches anything containing the text
        val searchQuery = "%$query%"

        lifecycleScope.launch {
            dao.searchDatabase(searchQuery).collect { list ->
                employeeAdapter.updateEmployees(list)
            }
        }
    }

    private fun loadEmployees() {
        lifecycleScope.launch {
            dao.getAllEmployees().collect { employees ->
                employeeAdapter.updateEmployees(employees)
            }
        }
    }
}