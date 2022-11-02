package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farmatecalphaversion.databinding.FragmentCadastroBinding;
import com.farmatecalphaversion.databinding.FragmentMenuBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class CadastroFragment extends Fragment {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/clientes/incluir/";
    private JSONArray restulJsonArray;
    private int sucesso = 0;
    public int  limparcampo = 0;
    private String mensagem = "", stringvazia ="", logado ="3";
    EditText edtNomeCompleto, edtEmail ,edtCPF;
    public String nomecompleto, email, cpf;

    ImageView imageView;

    private CadastroViewModel mViewModel;
    private FragmentCadastroBinding binding;

    public static CadastroFragment newInstance() {
        return new CadastroFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_cadastro, container, false);
        binding = FragmentCadastroBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        return root;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

        AndroidNetworking.initialize(getActivity().getApplicationContext());

        binding.btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               nomecompleto = binding.editTextNomeCompleto.getText().toString();
                email = binding.editTextEmail.getText().toString();
                cpf = binding.editTextCPF.getText().toString();

                if (nomecompleto.isEmpty() || email.isEmpty() || cpf.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                            .setTitle("Erro")
                            .setMessage("Favor preencher os campos")
                            .setPositiveButton("OK",null);
                    builder.create().show();
                }
                else {
                    apiCadastrar();
                if (limparcampo == 1) {
                    binding.editTextNomeCompleto.setText("");
                    binding.editTextCPF.setText("");
                    binding.editTextEmail.setText("");
                }
                }

            }
        });


    }

    protected void apiCadastrar() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtnomeCliente", nomecompleto.toString())
                .addBodyParameter("txtendereco", stringvazia.toString())
                .addBodyParameter("txtcep", stringvazia.toString())
                .addBodyParameter("txtcidade", stringvazia.toString())
                .addBodyParameter("txtbairro", stringvazia.toString())
                .addBodyParameter("txtuf", stringvazia.toString())
                .addBodyParameter("txtcpf", cpf.toString())
                .addBodyParameter("txtfoneCliente", stringvazia.toString())
                .addBodyParameter("txtemailCliente", email.toString())
                .addBodyParameter("txtusuario", cpf.toString())
                .addBodyParameter("txtsenha", cpf.toString())
                .addBodyParameter("txtlogado", logado.toString())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                if (jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 0) {
                                    mensagem = "Não foi possivel realizar o cadastro";
                                } else if (jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 1) {
                                    mensagem = "Dados cadastrados com sucesso";

                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder
                                        .setTitle("Aviso")
                                        .setMessage(mensagem)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                limparcampo = 1;
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (anError.getErrorCode() == 0) {
                                mensagem = "Problemas com a conexão!! \nTente Novamente.";
                            } else {
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());

                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                    .setTitle("Aviso")
                                    .setMessage(mensagem)
                                    .setPositiveButton("OK", null);
                            AlertDialog alert = builder.create();

                            alert.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("BridgeUpdateService", "error" + anError.getErrorCode() + anError.getErrorDetail());
                    }


                });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }


}