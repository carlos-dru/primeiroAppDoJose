package com.jose.meuprimeiroapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.widget.Button
import android.widget.EditText
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
                    var saldoAnterior: Double? = dataSnapshot.getValue(Double::class.java)
                    if (saldoAnterior != null) {
                        exibirPerguntaComCampoDeValor(this@homeActivity, "Digite o valor do depósito:", "Alerta",
                            callbackSucesso = { valor ->
                                if (valor == 0.0){
                                    exibirAlerta(this@homeActivity, "Por favor, digite um número maior que zero.")
                                } else {
                                    depositar(ref.child("saldo"), saldoAnterior, valor)
                                    exibirAlerta(
                                        this@homeActivity,
                                        "Depósito realizado com sucesso!"
                                    )
                                }
                            })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            ref.child("saldo").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var saldoAtual: Double? = dataSnapshot.getValue(Double::class.java)
                    if (saldoAtual != null) {
                        mensagemSaldo.text = "Seu saldo atual é: R$ $saldoAtual"
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

    fun exibirPerguntaComCampoDeValor(context: Context, texto: String, titulo: String, callbackSucesso: (Double) -> Unit) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(titulo)
            .setMessage(texto)

            val campoValor = EditText(context)
            campoValor.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            alertDialog.setView(campoValor)

            .setPositiveButton("Depositar") { dialog, _ ->
                // Ação a ser executada ao pressionar o botão "OK"
                dialog.dismiss()
                if (campoValor.text.toString().isNullOrEmpty()){
                    callbackSucesso(0.0)
                } else {
                    callbackSucesso(campoValor.text.toString().toDouble())
                }
            }
            .setNegativeButton("Cancelar"){dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }


    fun exibirAlertaComCampoValor(
        context: Context,
        sucessoCallback: (Double) -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Digite um valor")
        builder.setMessage("Digite um valor:")

        val input = EditText(context)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val userInput = input.text.toString().toDouble()
            sucessoCallback(userInput)
            dialog.dismiss()
        }

        builder.show()
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
