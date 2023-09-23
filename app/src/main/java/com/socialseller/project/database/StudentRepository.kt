package com.socialseller.project.database

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


class StudentRepository(private val dao: StudentDao) {
    val getStudentData: LiveData<List<StudentEntity>> = dao.getStudentData()
    val getStudentDataDescending: LiveData<List<StudentEntity>> = dao.getStudentDataDescending()


    suspend fun update(
        student: StudentEntity
    ) {
        dao.update(student)
    }

    suspend fun insert(student: StudentEntity){
        dao.insert(student)
    }

    suspend fun addStudent(student: StudentEntity) {
        dao.insert(student)
    }

    suspend fun delete(student: StudentEntity) {
        dao.delete(student)
    }
    fun searchDatabase(searchQuery: String): Flow<List<StudentEntity>>{
        return dao.searchDataBase(searchQuery)
    }
}