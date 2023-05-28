package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class cadastroActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var botaoLogin = findViewById<Button>(R.id.botaoLogin)
        var botaoCadastrar = findViewById<Button>(R.id.botaoCadastrar)
        var usuario = findViewById<EditText>(R.id.usuario)
        var senha = findViewById<EditText>(R.id.senha)
        var auth = Firebase.auth!!
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref: DatabaseReference = database.reference.child("root/usuarios")



        botaoLogin.setOnClickListener {
            if (usuario.text.toString().isNullOrEmpty() || senha.text.toString().isNullOrEmpty()){
                exibirAlerta(this, "Usuário ou senha incorretos.")
            }else {
                auth.signInWithEmailAndPassword(usuario.text.toString(), senha.text.toString())
                    .addOnSuccessListener {
                        val home = Intent(this, homeActivity::class.java)
                        home.putExtra("emailUsuario", usuario.text.toString())
                        home.putExtra("uidUsuario", auth.currentUser?.uid.toString())
                        startActivity(home)
                    }
                    .addOnFailureListener {
                        exibirAlerta(this, "Usuário ou senha incorretos.")
                    }
            }
        }
        botaoCadastrar.setOnClickListener {
            if (usuario.text.toString().isNullOrEmpty() || senha.text.toString().isNullOrEmpty()){
                exibirAlerta(this, "Por favor, preencha seu e-mail e senha.")
            }else {
                auth.createUserWithEmailAndPassword(usuario.text.toString(), senha.text.toString())
                    .addOnSuccessListener {
                        exibirAlerta(this, "Usuário cadastrado com sucesso!")
                        var idNovo = auth.currentUser?.uid.toString()
                        //exibirAlerta(this, "Novo id: $idNovo")
                        val refNovoUsuario: DatabaseReference = ref.child(idNovo)
                        val novoUsuario: HashMap<String, Any> = HashMap()
                        novoUsuario["nome"] = "Teste123"
                        novoUsuario["saldo"] = 100
                        refNovoUsuario.setValue(novoUsuario)
                            .addOnSuccessListener {
                                exibirAlerta(this, "Usuário cadastrado com sucesso!")
                            }
                            .addOnFailureListener{
                                exibirAlerta(this, "Ocorreu um erro ao cadastrar o usuário.")
                            }
                    }
                    .addOnFailureListener{
                        exibirAlerta(this, "Ocorreu um erro ao cadastrar o usuário.")
                    }
            }
        }
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