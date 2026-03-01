package org.freedu.roomdatabaseb7

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val department: String,
    val salary: Double,
    val email: String,
    val createdAt: Long = System.currentTimeMillis()
)