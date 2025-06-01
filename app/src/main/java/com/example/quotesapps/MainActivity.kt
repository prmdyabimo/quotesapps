package com.example.quotesapps

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        showLoginView()
    }

    private fun showLoginView() {
        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 100, 60, 100)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
        }

        val emailInput = EditText(this).apply {
            hint = "Email"
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 40
            }
        }

        val passwordInput = EditText(this).apply {
            hint = "Password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 60
            }
        }

        val loginButton = Button(this).apply {
            text = "Login"
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        rootLayout.addView(emailInput)
        rootLayout.addView(passwordInput)
        rootLayout.addView(loginButton)

        setContentView(rootLayout)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showQuotesView()
                    } else {
                        Toast.makeText(
                            this,
                            "Login gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun showQuotesView() {
        val quotesList = listOf(
            "“Be yourself; everyone else is already taken.” ― Oscar Wilde",
            "“Two things are infinite: the universe and human stupidity; and I'm not sure about the universe.” ― Albert Einstein",
            "“So many books, so little time.” ― Frank Zappa",
            "“A room without books is like a body without a soul.” ― Marcus Tullius Cicero",
            "“Be the change that you wish to see in the world.” ― Mahatma Gandhi",
            "“In three words I can sum up everything I've learned about life: it goes on.” ― Robert Frost",
            "“If you tell the truth, you don't have to remember anything.” ― Mark Twain",
            "“A friend is someone who knows all about you and still loves you.” ― Elbert Hubbard",
            "“To live is the rarest thing in the world. Most people exist, that is all.” ― Oscar Wilde",
            "“Without music, life would be a mistake.” ― Friedrich Nietzsche"
        )

        val scrollView = ScrollView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val heading = TextView(this).apply {
            text = "Daftar Quotes"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 30
            }
        }
        container.addView(heading)

        quotesList.forEach { quote ->
            val quoteTextView = TextView(this).apply {
                text = quote
                textSize = 16f
                setPadding(0, 20, 0, 20)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            container.addView(quoteTextView)
        }

        val logoutButton = Button(this).apply {
            text = "Logout"
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 40
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
        container.addView(logoutButton)

        logoutButton.setOnClickListener {
            auth.signOut()
            showLoginView()
        }

        scrollView.addView(container)
        setContentView(scrollView)
    }
}
