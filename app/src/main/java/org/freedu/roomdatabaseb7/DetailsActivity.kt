package org.freedu.roomdatabaseb7

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.freedu.roomdatabaseb7.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var database: EmployeeDatabase
    private lateinit var dao: EmployeeDao
    private var employeeId: Int = 0
    private var currentEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = EmployeeDatabase.getDatabase(this)
        dao = database.employeeDao()
        employeeId = intent.getIntExtra("employee_id", 0)

        setupButtons()
        loadEmployeeDetails()
    }

    private fun setupButtons() {
        binding.btnEdit.setOnClickListener { enableEditing() }
        binding.btnDelete.setOnClickListener { showDeleteConfirmation() }
    }

    private fun loadEmployeeDetails() {
        lifecycleScope.launch {
            currentEmployee = dao.getEmployeeById(employeeId)
            currentEmployee?.let { displayEmployeeDetails(it) }
        }
    }

    private fun displayEmployeeDetails(employee: Employee) {
        binding.tvDetailName.text = employee.name
        binding.tvDetailDept.text = employee.department
        binding.tvDetailSalary.text = "$${employee.salary}"
        binding.tvDetailEmail.text = employee.email
    }

    private fun enableEditing() {
        // Populate inputs
        binding.etEditName.setText(currentEmployee?.name)
        binding.etEditDept.setText(currentEmployee?.department)
        binding.etEditSalary.setText(currentEmployee?.salary.toString())
        binding.etEditEmail.setText(currentEmployee?.email)

        // Toggle UI
        binding.layoutDetails.visibility = View.GONE
        binding.layoutEdit.visibility = View.VISIBLE

        // Change Delete Button to Cancel
        binding.btnDelete.text = "Cancel"
        binding.btnDelete.setTextColor(Color.DKGRAY)
        binding.btnDelete.setStrokeColorResource(android.R.color.darker_gray)
        binding.btnDelete.setOnClickListener { disableEditing() }

        // Change Edit Button to Save
        binding.btnEdit.text = "Save Changes"
        binding.btnEdit.setOnClickListener { saveEditedEmployee() }
    }

    private fun disableEditing() {
        binding.layoutDetails.visibility = View.VISIBLE
        binding.layoutEdit.visibility = View.GONE

        // Restore Delete
        binding.btnDelete.text = "Delete"
        binding.btnDelete.setTextColor(Color.parseColor("#F44336"))
        binding.btnDelete.setStrokeColorResource(android.R.color.transparent) // or your original color
        binding.btnDelete.setOnClickListener { showDeleteConfirmation() }

        // Restore Edit
        binding.btnEdit.text = "Edit Profile"
        binding.btnEdit.setOnClickListener { enableEditing() }
    }

    private fun saveEditedEmployee() {
        currentEmployee?.let { employee ->
            val updated = employee.copy(
                name = binding.etEditName.text.toString().trim(),
                department = binding.etEditDept.text.toString().trim(),
                salary = binding.etEditSalary.text.toString().toDoubleOrNull() ?: 0.0,
                email = binding.etEditEmail.text.toString().trim()
            )

            lifecycleScope.launch {
                dao.updateEmployee(updated)
                currentEmployee = updated
                displayEmployeeDetails(updated)
                disableEditing()
                Toast.makeText(this@DetailsActivity, "Updated successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Employee")
            .setMessage("Are you sure you want to delete ${currentEmployee?.name}?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Yes") { _, _ -> deleteEmployee() }
            .show()
    }

    private fun deleteEmployee() {
        currentEmployee?.let {
            lifecycleScope.launch {
                dao.deleteEmployee(it)
                finish()
            }
        }
    }
}