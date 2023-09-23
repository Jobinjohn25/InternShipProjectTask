package com.socialseller.project.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.socialseller.project.R
import com.socialseller.project.database.StudentEntity
import com.socialseller.project.database.StudentViewModel
import com.socialseller.project.fragment.AboutFragment
import com.socialseller.project.fragment.DashboardFragment
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


class HomeActivity : AppCompatActivity() {
    //initiating variables
    private lateinit var btnInsert: Button
    private lateinit var mStudentViewModel: StudentViewModel
    private lateinit var contentResolver: ContentResolver
    private lateinit var btnImport: Button
    private lateinit var signOut: Button
    private lateinit var toolBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var navigatioView: NavigationView
    private lateinit var frameLayout: FrameLayout
    private lateinit var client: GoogleSignInClient
    private var previousMenuItem: MenuItem? = null
    private var studentList = arrayListOf<StudentEntity>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        contentResolver = applicationContext.contentResolver

        // For Signing out
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(this, options)
        signOut = findViewById(R.id.signOut)
        signOut.setOnClickListener {
            Firebase.auth.signOut()
            client.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        frameLayout = findViewById(R.id.frameLayout)
        navigatioView = findViewById(R.id.navigationView)
        toolBar = findViewById(R.id.toolBar)
        btnImport = findViewById(R.id.btnImport)
        btnInsert = findViewById(R.id.btnInsert)

        btnInsert.setOnClickListener {
            drawerLayout.closeDrawers()
            intent = Intent(this, InsertRecordActivity::class.java)
            startActivity(intent)
        }


//        Setting user name
        val auth = FirebaseAuth.getInstance()
        val headerLayout = navigatioView.inflateHeaderView(R.layout.drawer_header)
        val userName = headerLayout.findViewById<TextView>(R.id.userName)
        userName.text = auth.currentUser?.displayName.toString()


        btnImport.setOnClickListener {
            resultLauncher.launch("*/*")
        }

        //set user view model
        mStudentViewModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        //call Function to setup toolbar
        setupToolBar()

        //Set DashboardFragment
        openDashboard()

        //setting up  Hamburger Icon
        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //adding listeners to menu item in Navigation View
        navigatioView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }

                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment())
                        .commit()
                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()

                }
            }
            return@setNavigationItemSelectedListener true

        }

    }

    //open file picker
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) {
            Toast.makeText(this, "Nothing Selected", Toast.LENGTH_SHORT).show()
        } else {
            it.also {
                //parse csv file
                if (readCsv(it)) {
                    insertDataToDataBase(studentList)
                    studentList.clear()
                    Toast.makeText(this, "Data  Imported Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Duplicate Data or invalid file", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun insertDataToDataBase(studentList: ArrayList<StudentEntity>) {
        for (student in studentList) {
            mStudentViewModel
            mStudentViewModel.addStudent(student)
        }

    }

    //function to read csv file
    private fun readCsv(uri: Uri): Boolean {
        contentResolver.openInputStream(uri).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readLine()
                var line: String? = reader.readLine()
                while (line != null) {
                    val dataList = line.split(',', ignoreCase = false)
                    val id = dataList[0]
                    val name = dataList[1]
                    val rollNo = dataList[2]
                    val email = dataList[3]
                    val phone = dataList[4]
                    val address = dataList[5]
                    val pincode = dataList[6]
                    if(checkId(id.trim().toInt())){
                        val student = StudentEntity(
                            id.trim().toInt(),
                            name.trim().removeSurrounding("\""),
                            rollNo.trim().removeSurrounding("\""),
                            email.trim().removeSurrounding("\""),
                            phone.trim().removeSurrounding("\""),
                            address.trim().removeSurrounding("\""),
                            pincode.trim().removeSurrounding("\""),
                        )
                        studentList.add(student)
                    }
                    else{
                        return false
                    }

                    line = reader.readLine()
                }
                return true
            }
        }

    }

    private fun checkId(id: Int): Boolean {
        if(studentList.isEmpty()){
            return true
        }
        else{
            for (i in studentList){
                return i.studentId != id
            }
        }
        return true
    }


    //function to open dashboard fragment
    private fun openDashboard() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, DashboardFragment())
            .commit()
        supportActionBar?.title = "Dashboard"
        navigatioView.setCheckedItem(R.id.dashboard)

    }

    //function to setup tool bar
    private fun setupToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Dashboard"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //function to make hamburger icon functional
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    //setting back button functionality
    override fun onBackPressed() {

        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }


}