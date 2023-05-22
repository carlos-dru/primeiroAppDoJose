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
        val extras = intent.extras!!
        var nomeUsuario = extras.getString("emailUsuario").toString()
        var uidUsuario = extras.getString("uidUsuario").toString()
        textoBoasVindas.text = textoBoasVindas.text.toString() + " $nomeUsuario!"// + " uid: $uidUsuario"


    }
}
