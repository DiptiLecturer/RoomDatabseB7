package org.freedu.roomdatabaseb7

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.freedu.roomdatabaseb7.databinding.ItemEmployeeBinding


class EmployeeAdapter(
    private var employees: List<Employee>,
    private val onItemClick: (Employee) -> Unit
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    // Helper function to refresh the list during search or database changes
    @SuppressLint("NotifyDataSetChanged")
    fun updateEmployees(newEmployees: List<Employee>) {
        employees = newEmployees
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmployeeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employees[position], onItemClick)
    }

    override fun getItemCount() = employees.size

    class EmployeeViewHolder(
        private val binding: ItemEmployeeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: Employee, onItemClick: (Employee) -> Unit) {
            // Display data
            binding.tvEmpName.text = employee.name
            binding.tvEmpRole.text = employee.department // Fixed duplicate department text
            binding.tvEmpEmail.text = employee.email

            // Handle the main item click (navigates to details)
            binding.root.setOnClickListener {
                onItemClick(employee)
            }

            // Optional: If you want specific actions for these buttons later,
            // you should pass different lambdas. For now, they link to details.
            binding.btnCall.setOnClickListener { onItemClick(employee) }
            binding.btnEmail.setOnClickListener { onItemClick(employee) }
            binding.ivMore.setOnClickListener { onItemClick(employee) }
        }
    }
}