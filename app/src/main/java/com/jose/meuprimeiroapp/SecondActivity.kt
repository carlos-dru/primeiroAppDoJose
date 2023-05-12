package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        var textoBoasVindas = findViewById<TextView>(R.id.mensagemSucesso)
        if (getIntent().hasExtra("nomeUsuario")){
            val extras = intent.extras!!
            var nomeUsuario = extras.getString("nomeUsuario").toString()
            textoBoasVindas.text = plus(textoBoasVindas.text.toString(), " $nomeUsuario!")
        }

    }
}

private fun plus(s1: String, s2: String): String? {
    return s1 + s2
}
