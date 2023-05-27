package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
class SecondActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        var textoBoasVindas = findViewById<TextView>(R.id.mensagemSucesso)
        val extras = intent.extras!!
        var emailUsuario = extras.getString("emailUsuario").toString()
        var uidUsuario = extras.getString("uidUsuario").toString()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        //val ref: DatabaseReference = database.reference.child("root").child("usuarios").child(uidUsuario)
        val ref: DatabaseReference = database.reference.child("root/usuarios/$uidUsuario")
        ref.child("nome").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name: String? = dataSnapshot.getValue(String::class.java)
                if (name != null) {
                    textoBoasVindas.text = textoBoasVindas.text.toString() + " $name!"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })

    }
}
