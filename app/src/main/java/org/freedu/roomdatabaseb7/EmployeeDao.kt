package org.freedu.roomdatabaseb7


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insertEmployee(employee: Employee): Long

    @Query("SELECT * FROM employees ORDER BY createdAt DESC")
    fun getAllEmployees(): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getEmployeeById(id: Int): Employee?

    @Update
    suspend fun updateEmployee(employee: Employee)

    @Delete
    suspend fun deleteEmployee(employee: Employee)

    @Query("DELETE FROM employees WHERE id = :id")
    suspend fun deleteEmployeeById(id: Int)

    @Query("SELECT * FROM employees WHERE name LIKE :searchQuery OR department LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<Employee>>
}