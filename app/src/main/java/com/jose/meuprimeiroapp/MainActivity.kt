package com.jose.meuprimeiroapp

import android.app.AlertDialog
import android.content.Context
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jose.meuprimeiroapp.R.id.botaoLogin
import com.jose.meuprimeiroapp.R.id.usuario
import com.jose.meuprimeiroapp.R.id.senha
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var botaoLogin = findViewById<Button>(botaoLogin)
        var botaoCadastrar = findViewById<Button>(R.id.botaoCadastrar)
        var usuario = findViewById<EditText>(usuario)
        var senha = findViewById<EditText>(senha)
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
                        val refNovoUsuario: DatabaseReference = ref.child(idNovo)
                        val novoUsuario: HashMap<String, Any> = HashMap()
                        novoUsuario["email"] = usuario.text.toString()
                        novoUsuario["saldo"] = 0.0
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