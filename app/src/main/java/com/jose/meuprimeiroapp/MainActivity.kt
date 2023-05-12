package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.jose.meuprimeiroapp.R.id.botaoLogin
import com.jose.meuprimeiroapp.R.id.usuario
import com.jose.meuprimeiroapp.R.id.mensagemLogin
import com.jose.meuprimeiroapp.R.id.senha

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var botao = findViewById<Button>(botaoLogin)
        var usuario = findViewById<EditText>(usuario)
        var senha = findViewById<EditText>(senha)
        var mensagemLogin = findViewById<TextView>(mensagemLogin)
        botao.setOnClickListener {
            if (usuario.text.toString() != "Drumond1" || senha.text.toString() != "8009"){
                mensagemLogin.isVisible = true
                mensagemLogin.text = "Usuário ou senha incorretos."
            }
            else {
               // mensagemLogin.isVisible = true
               // mensagemLogin.text = "Seja bem-vindo, Zekão!"
                val home = Intent(this, SecondActivity::class.java)
                home.putExtra("nomeUsuario", usuario.text.toString())
                startActivity(home)
            }
        }
    }
}