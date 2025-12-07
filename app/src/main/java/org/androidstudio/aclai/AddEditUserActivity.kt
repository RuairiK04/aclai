package org.androidstudio.aclai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class AddEditUserActivity : AppCompatActivity() {

    private lateinit var usernameEdt: EditText
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: UserViewModel
    private var userID = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_user)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[UserViewModel::class.java]

        usernameEdt = findViewById(R.id.idEdtUsername)
        emailEdt = findViewById(R.id.idEdtEmail)
        passwordEdt = findViewById(R.id.idEdtPassword)
        saveBtn = findViewById(R.id.idBtnSaveUser)

        val userType = intent.getStringExtra("userType")
        if (userType == "Edit") {
            val username = intent.getStringExtra("username")
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")
            userID = intent.getIntExtra("userId", -1)

            saveBtn.text = "Update User"
            usernameEdt.setText(username)
            emailEdt.setText(email)
            passwordEdt.setText(password)
        } else {
            saveBtn.text = "Save User"
        }

        saveBtn.setOnClickListener {
            val username = usernameEdt.text.toString()
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = UserModel(username, password, email)
                if (userType == "Edit") {
                    user.id = userID
                    viewModel.updateUser(user)
                    Toast.makeText(this, "User Updated", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.addUser(user)
                    Toast.makeText(this, "$username Added", Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
    }
}