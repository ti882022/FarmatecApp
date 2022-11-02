package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/usuarios/conectar/";
    private JSONArray restulJsonArray;
    private int logado=0 ,resultado;
    private String mensagem = "", strusuario = "", stremail = "", strnomecompleto = "";
    EditText edtUsuario, edtSenha;
    Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.editTextUsuario);

        edtSenha = (EditText) findViewById(R.id.editTextSenha);

        btnlogin = (Button)findViewById(R.id.btnLogin);

        AndroidNetworking.initialize(getApplicationContext());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario,senha;

                usuario = edtUsuario.getText().toString();
                senha = edtSenha.getText().toString();

                if (usuario.isEmpty() || senha.isEmpty()){

                    AlertDialog.Builder builder =new
                            AlertDialog.Builder(Login.this).setTitle("Erro").setMessage("Favor preencher os campos").
                            setPositiveButton("ok", null);

                    builder.create().show();
                }
                else{
                    sendApi();
                }

            }
        });

    }
    protected void sendApi() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtusuario", edtUsuario.getText().toString())
                .addBodyParameter("txtsenha", edtSenha.getText().toString())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject != null) {
                                restulJsonArray = jsonObject.getJSONArray("RetornoDados");
                                JSONObject jsonObj = null;
                                jsonObj = restulJsonArray.getJSONObject(0);
                                resultado = jsonObj.getInt("resultado");

                                if (resultado == 1) {

                                    for (int i = 0; i < restulJsonArray.length(); i++) {
                                        jsonObj = restulJsonArray.getJSONObject(i);
                                        logado = jsonObj.getInt("plogado");
                                        stremail = jsonObj.getString("pemail");
                                        strnomecompleto = jsonObj.getString("pnome");
                                    }

                                    switch (logado) {
                                        case 1:
                                            mensagem = "Bem vindo ao Sistema";
                                            break;
                                        case 2:
                                            mensagem = "Usuário já esta conectado";
                                            break;
                                    }
                                }
                                else{
                                    mensagem = "Usuário ou senha invalidos";
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this)
                                        .setTitle("Aviso")
                                        .setMessage(mensagem).
                                        setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (logado == 1) {
                                                    Intent base = new Intent(getApplicationContext(),
                                                            BaseMenu.class);
                                                    base.putExtra("usuario", strusuario.toString());
                                                    base.putExtra("email", stremail.toString());
                                                    base.putExtra("nomecompleto", strnomecompleto.toString());
                                                    startActivity(base);
                                                    finish();
                                                }
                                            }
                                        });

                                builder.create().show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (anError.getErrorCode() == 0) {
                                mensagem = "Problemas com conexão!! \nTente novamente.";
                            } else {
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                                if (jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 0) {
                                    mensagem = "Usuário ou senha inválidos";
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this)
                                    .setTitle("Aviso")
                                    .setMessage(mensagem).
                                    setPositiveButton("ok", null);
                            builder.create().show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("BridgeUpdateService", "error" + anError.getErrorCode() + anError.getErrorDetail());

                    }
                });
    }
}