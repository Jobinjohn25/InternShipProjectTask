package com.socialseller.project.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.socialseller.project.R
import com.socialseller.project.database.StudentEntity
import com.socialseller.project.database.StudentViewModel

class InsertRecordActivity : AppCompatActivity() {
    private lateinit var mStudentViewModel: StudentViewModel
    lateinit var etInsertId: EditText
    lateinit var etInsertName: EditText
    lateinit var etInsertRollno: EditText
    lateinit var etInsertEmail: EditText
    lateinit var etInsetPhone: EditText
    lateinit var etInsertAdress: EditText
    lateinit var etInsertPincode: EditText
    lateinit var btnInsert: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_record)
        //getting viewmodel of the database
        mStudentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        etInsertId = findViewById(R.id.etInsertId)
        etInsertName = findViewById(R.id.etInsertName)
        etInsertRollno = findViewById(R.id.etInsertRollNo)
        etInsertEmail = findViewById(R.id.etInsertEmail)
        etInsetPhone = findViewById(R.id.etInsertPhone)
        etInsertAdress = findViewById(R.id.etInsertaddress)
        etInsertPincode = findViewById(R.id.etInsertPincode)
        btnInsert = findViewById(R.id.btnInsertRecord)
        btnInsert.setOnClickListener {
            if (TextUtils.isEmpty(etInsertId.text.toString()) && TextUtils.isEmpty(etInsertRollno.text.toString())) {
                Toast.makeText(
                    this,
                    "Please enter data in fields id and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val student = StudentEntity(
                    etInsertId.text.toString().toInt(),
                    etInsertName.text.toString(),
                    etInsertRollno.text.toString(),
                    etInsertEmail.text.toString(),
                    etInsetPhone.text.toString(),
                    etInsertAdress.text.toString(),
                    etInsertPincode.text.toString()
                )
                mStudentViewModel.insertStudent(student)
                Toast.makeText(
                    this,
                    "Data entered Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

    }
}