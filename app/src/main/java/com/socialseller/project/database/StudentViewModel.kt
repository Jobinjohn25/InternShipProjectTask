package com.socialseller.project.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class StudentViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<StudentEntity>>
    private val studentRepo: StudentRepository
    val readAllDataDescending: LiveData<List<StudentEntity>>

    init {
        val dao = StudentDatabase.getDatabase(application).studentDao()
        studentRepo = StudentRepository(dao)
        readAllData = studentRepo.getStudentData
        readAllDataDescending = studentRepo.getStudentDataDescending
    }

    fun addStudent(student: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepo.addStudent(student)
        }
    }

    fun updateStudent(student: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepo.update(student)
        }
    }
    fun insertStudent(student: StudentEntity){
        viewModelScope.launch(Dispatchers.IO) {
            studentRepo.insert(student)
        }
    }
    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepo.delete(student)
        }
    }
    fun searchDatabase(searchQuery: String): LiveData<List<StudentEntity>> {
        return studentRepo.searchDatabase(searchQuery).asLiveData()
    }
}