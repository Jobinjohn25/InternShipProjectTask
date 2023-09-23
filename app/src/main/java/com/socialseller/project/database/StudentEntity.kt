package com.socialseller.project.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class StudentEntity (
    @PrimaryKey val studentId:Int,
            @ColumnInfo(name="name") val name: String?,
            @ColumnInfo(name="roll_number") val rollNumber: String?,
            @ColumnInfo(name="email") val email: String?,
            @ColumnInfo(name="phone") val phone: String?,
            @ColumnInfo(name="address") val address: String?,
            @ColumnInfo(name="pincode") val pincode: String?,
)