package com.socialseller.project.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.concurrent.Flow

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(studentEntity: StudentEntity)

    @Delete
    suspend fun delete(studentEntity: StudentEntity)

    @Update
    suspend fun update(studentEntity: StudentEntity)

    @Query("SELECT * from student_table ORDER BY studentId")
    fun getStudentData(): LiveData<List<StudentEntity>>

    @Query("SELECT * from student_table ORDER BY studentId DESC")
    fun getStudentDataDescending(): LiveData<List<StudentEntity>>

    @Query("DELETE from student_table WHERE studentId= :studentId ")
    fun deleteEntry(studentId: Int)

    @Query("SELECT * from student_table WHERE name LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<StudentEntity>>


}