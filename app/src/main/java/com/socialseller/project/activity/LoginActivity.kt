package com.socialseller.project.activity

// Login Activity

import android.annotation.SuppressLint
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.socialseller.project.R

class LoginActivity : AppCompatActivity() {
    // Initialing variables
    lateinit var btnLogin: Button
    lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //CHeck if user is already logged in
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_login)

            // Defining Google sign in options and GoogleSign in client
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            client = GoogleSignIn.getClient(this, options)


            progressIndicator = findViewById(R.id.progressIndicator)
            progressIndicator.isVisible = false

            btnLogin = findViewById(R.id.btnLogin)
            btnLogin.setOnClickListener {
                signIn()
            }
        }


    }

    // Function to start Sign in Intent
    private fun signIn() {
        progressIndicator.isVisible = true
        btnLogin.isVisible = false
        val signInIntent = client.signInIntent
        launcher.launch(signInIntent)
    }

    // Launches a screen to show logged in users
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                progressIndicator.isVisible = false
                btnLogin.isVisible = true
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUi(account)
            }
        } else {
            Toast.makeText(this@LoginActivity, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi(account: GoogleSignInAccount) {

        val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(crendential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("username",account.displayName.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity, it.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}
