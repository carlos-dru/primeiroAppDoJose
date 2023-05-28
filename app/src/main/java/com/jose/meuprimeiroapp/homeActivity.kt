package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class homeActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        //Exibindo nome e saldo do usuário
        var textoBoasVindas = findViewById<TextView>(R.id.mensagemBoasVindas)
        var mensagemSaldo = findViewById<TextView>(R.id.mensagemSaldo)
        var botaoDepositar = findViewById<Button>(R.id.botaoDepositar)
        val extras = intent.extras!!
        var uidUsuario = extras.getString("uidUsuario").toString()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref: DatabaseReference = database.reference.child("root/usuarios/$uidUsuario")
        ref.child("nome").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nome: String? = dataSnapshot.getValue(String::class.java)
                if (nome != null) {
                    textoBoasVindas.text = textoBoasVindas.text.toString() + " $nome!"
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        ref.child("saldo").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var saldo: Double? = dataSnapshot.getValue(Double::class.java)
                if (saldo != null) {
                    mensagemSaldo.text = mensagemSaldo.text.toString() + " R$$saldo"
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        //Depositando na conta
        botaoDepositar.setOnClickListener {
            ref.child("saldo").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var saldo: Double? = dataSnapshot.getValue(Double::class.java)
                    if (saldo != null) {
                        depositar(ref.child("saldo"), saldo, 50.0)
                        mensagemSaldo.text = mensagemSaldo.text.toString() + " R$$saldo"
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        }
    }

    fun depositar (path: DatabaseReference,saldoAtual: Double ,valorDeposito: Double){
        path.setValue(saldoAtual + valorDeposito)
    }

    fun exibirPerguntaComCampoDeValor(context: Context, texto: String, titulo: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(texto)
            .setPositiveButton("OK") { dialog, _ ->
                // Ação a ser executada ao pressionar o botão "OK"
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    fun exibirAlerta(context: Context, texto: String) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Alerta")
            .setMessage(texto)
            .setPositiveButton("OK") { dialog, _ ->
                // Ação a ser executada ao pressionar o botão "OK"
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }
}
