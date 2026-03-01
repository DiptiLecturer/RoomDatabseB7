package org.freedu.roomdatabaseb7

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.freedu.roomdatabaseb7.databinding.ActivityAddBinding



class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var database: EmployeeDatabase
    private lateinit var dao: EmployeeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = EmployeeDatabase.getDatabase(this)
        dao = database.employeeDao()

        binding.btnSaveEmployee.setOnClickListener {
            saveEmployee()
        }
    }

    private fun saveEmployee() {
        // Reset errors
        binding.tilAddName.error = null
        binding.tilAddDept.error = null
        binding.tilAddSalary.error = null
        binding.tilAddEmail.error = null

        val name = binding.etAddName.text.toString().trim()
        val department = binding.etAddDept.text.toString().trim()
        val salaryText = binding.etAddSalary.text.toString().trim()
        val email = binding.etAddEmail.text.toString().trim()

        // Comprehensive Validation
        var isValid = true

        if (name.isEmpty()) {
            binding.tilAddName.error = "Name is required"
            isValid = false
        }
        if (department.isEmpty()) {
            binding.tilAddDept.error = "Department is required"
            isValid = false
        }
        if (salaryText.isEmpty()) {
            binding.tilAddSalary.error = "Salary is required"
            isValid = false
        }
        if (email.isEmpty()) {
            binding.tilAddEmail.error = "Email is required"
            isValid = false
        }

        if (!isValid) return

        val salary = salaryText.toDoubleOrNull()
        if (salary == null) {
            binding.tilAddSalary.error = "Invalid salary amount"
            return
        }

        val employee = Employee(
            name = name,
            department = department,
            salary = salary,
            email = email
        )

        lifecycleScope.launch {
            try {
                dao.insertEmployee(employee)
                Toast.makeText(this@AddActivity, "Employee Saved!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddActivity, "Error saving employee", Toast.LENGTH_SHORT).show()
            }
        }
    }
}