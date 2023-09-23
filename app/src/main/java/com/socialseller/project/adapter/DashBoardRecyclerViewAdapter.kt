package com.socialseller.project.adapter

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.socialseller.project.R
import com.socialseller.project.database.StudentEntity
import com.socialseller.project.database.StudentViewModel


class DashBoardRecyclerViewAdapter(val context: Context, val mStudentViewModel: StudentViewModel) :
    RecyclerView.Adapter<DashBoardRecyclerViewAdapter.DashboardViewHolder>() {
    private var studentList = emptyList<StudentEntity>()
    //variable to toggle between edit and save in button
    private var btnEditTxt = "Save"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {


        val student = studentList[position]
        holder.txtId.text = student.studentId.toString()
        holder.etName.text.clear()
        holder.etName.text.append(student.name)
        holder.etRollNumber.text.clear()
        holder.etRollNumber.text.append(student.rollNumber)
        holder.etEmail.text.clear()
        holder.etEmail.text.append(student.email)
        holder.etPhone.text.clear()
        holder.etPhone.text.append(student.phone)
        holder.etAddress.text.clear()
        holder.etAddress.text.append(student.address)
        holder.etPincode.text.clear()
        holder.etPincode.text.append(student.pincode)

        //Applying Click listener to the button
        holder.btnEdit.setOnClickListener {
            if (holder.btnEdit.text == "Edit") {

                //changing text when button is clicked
                holder.btnEdit.text = btnEditTxt

                //Enable Edit Text
                holder.etName.isEnabled = true
                holder.etEmail.isEnabled = true
                holder.etPhone.isEnabled = true
                holder.etAddress.isEnabled = true
                holder.etPincode.isEnabled = true

                //Set background color when in edit mode
                holder.etName.setBackgroundColor(Color.WHITE)
                holder.etEmail.setBackgroundColor(Color.WHITE)
                holder.etPhone.setBackgroundColor(Color.WHITE)
                holder.etAddress.setBackgroundColor(Color.WHITE)
                holder.etPincode.setBackgroundColor(Color.WHITE)

                btnEditTxt = "Edit"
            } else {

                //changing text when button is clicked
                holder.btnEdit.text = btnEditTxt

                //Enable Edit Text
                holder.etName.isEnabled = false
                holder.etEmail.isEnabled = false
                holder.etPhone.isEnabled = false
                holder.etAddress.isEnabled = false
                holder.etPincode.isEnabled = false

                //Set background color when in edit mode
                holder.etName.setBackgroundColor(Color.alpha(0))
                holder.etEmail.setBackgroundColor(Color.alpha(0))
                holder.etPhone.setBackgroundColor(Color.alpha(0))
                holder.etAddress.setBackgroundColor(Color.alpha(0))
                holder.etPincode.setBackgroundColor(Color.alpha(0))

                updateData(student, holder)

            }

        }
        holder.btnDelete.setOnClickListener {
            mStudentViewModel.deleteStudent(student)
            Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(student: StudentEntity,holder: DashboardViewHolder ) {
        val studentId = student.studentId
        val name = holder.etName.text.toString()
        val rollNo = holder.etRollNumber.text.toString()

        val email = holder.etEmail.text.toString()
        val phone = holder.etPhone.text.toString()
        val address = holder.etAddress.text.toString()
        val pincode = holder.etPincode.text.toString()

        if(inputCheck(name, email, phone, address, pincode)){
            val student = StudentEntity(studentId,name,rollNo,email,phone, address, pincode)
            mStudentViewModel.updateStudent(student)
            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
            btnEditTxt = "save"
        }else{
            Toast.makeText(context,"Please Fill all Fields",Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(
        name: String,
        email: String,
        phone: String,
        address: String,
        pincode: String
    ): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(
            address
        ) && TextUtils.isEmpty(pincode))
    }


    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.txtId)
        val etName: EditText = view.findViewById(R.id.etName)
        val etRollNumber: EditText = view.findViewById(R.id.etRollNo)
        val etEmail: EditText = view.findViewById(R.id.etEmail)
        val etPhone: EditText = view.findViewById(R.id.etPhone)
        val etAddress: EditText = view.findViewById(R.id.etaddress)
        val etPincode: EditText = view.findViewById(R.id.etPincode)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    fun setData(student: List<StudentEntity>) {
        this.studentList = student
        notifyDataSetChanged()
    }
}