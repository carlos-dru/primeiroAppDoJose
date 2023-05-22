package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jose.meuprimeiroapp.R.id.botaoLogin
import com.jose.meuprimeiroapp.R.id.usuario
import com.jose.meuprimeiroapp.R.id.mensagemLogin
import com.jose.meuprimeiroapp.R.id.senha

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var botaoLogin = findViewById<Button>(botaoLogin)
        var botaoCadastrar = findViewById<Button>(R.id.botaoCadastrar)
        var usuario = findViewById<EditText>(usuario)
        var senha = findViewById<EditText>(senha)
        var mensagemLogin = findViewById<TextView>(mensagemLogin)
        var auth = Firebase.auth!!
        botaoLogin.setOnClickListener {
            if (usuario.text.toString().isNullOrEmpty() || senha.text.toString().isNullOrEmpty()){
                mensagemLogin.isVisible = true
                mensagemLogin.text = "Usuário ou senha incorretos."
            }else {
                auth.signInWithEmailAndPassword(usuario.text.toString(), senha.text.toString())
                    .addOnSuccessListener {
                        val home = Intent(this, SecondActivity::class.java)
                        home.putExtra("emailUsuario", usuario.text.toString())
                        startActivity(home)
                    }
                    .addOnFailureListener {
                        mensagemLogin.isVisible = true
                        mensagemLogin.text = "Usuário ou senha incorretos."
                    }
            }
        }
        botaoCadastrar.setOnClickListener {
            if (usuario.text.toString().isNullOrEmpty() || senha.text.toString().isNullOrEmpty()){
                mensagemLogin.isVisible = true
                mensagemLogin.text = "Por favor, preencha seu e-mail e senha."
            }else {
                auth.createUserWithEmailAndPassword(usuario.text.toString(), senha.text.toString())
                    .addOnSuccessListener {
                        mensagemLogin.isVisible = true
                        mensagemLogin.text = "Usuário cadastrado com sucesso!"
                    }
                    .addOnFailureListener{
                        mensagemLogin.isVisible = true
                        mensagemLogin.text = "Ocorreu um erro ao cadastrar o usuário."
                    }
            }
        }
    }
}