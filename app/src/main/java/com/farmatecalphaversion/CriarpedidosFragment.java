package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farmatecalphaversion.databinding.FragmentCriarpedidosBinding;
import com.farmatecalphaversion.ui.ConvertStringToJson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CriarpedidosFragment extends Fragment {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/pedidos/incluir/";
    private String apiPathconsulta = "http://10.38.45.24:8080/farmatec-api/produtos/listarcod/";
    private JSONArray restulJsonArray;
    private int sucesso = 0, lprodutoint,lqtdeint;
    private double lprecodouble;
    public int  limparcampo = 0;
    private String mensagem = "", stringvazia ="", logado ="3", datapande="0", lproduto="",lpreco="",lqtde="", resultadolista="", formadepagaento="0";
    String edtnomecliente, edtcanal ,edtforma, edtcodfuncionarios, edtcodproduto, edtquantidade, json;

    private CriarpedidosViewModel mViewModel;
    private FragmentCriarpedidosBinding binding;
    public static CriarpedidosFragment newInstance() {
        return new CriarpedidosFragment();
    }


    public ArrayList<CarregarProdutosPedido> arraylistpedidos = new ArrayList<CarregarProdutosPedido>();
    JSONArray temparr = new JSONArray();
    public JsonObject temp = new JsonObject();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCriarpedidosBinding.inflate(inflater,container,false);

        View root = binding.getRoot();

        return root;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);

       // ListView listview = (ListView) view.findViewById(R.id.GridViewPedidos);
        AndroidNetworking.initialize(getActivity().getApplicationContext());

        binding.btnInserirPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // ArrayAdapter<CarregarProdutosPedido> ArrayViewPedidos = new ArrayAdapter<CarregarProdutosPedido>(getActivity(), android.R.layout.simple_list_item_1,arraylistpedidos);
                CarregarProdutosPedido Produtos = new CarregarProdutosPedido();
                edtcodproduto = binding.editTextCodProduto.getText().toString();
                edtquantidade = binding.editTextQuantidade.getText().toString();
                apiConsultaPreco();
                AdapterPedidos adapterPedidos = new AdapterPedidos(requireContext(),arraylistpedidos);
                binding.ListViewPedidos.setAdapter(adapterPedidos);

                /*Produtos.fpreco = "10,99";
                Produtos.fproduto  = "1";
                Produtos.fquantidade = "2";
                arraylistpedidos.add(new CarregarProdutosPedido(Produtos.fproduto,Produtos.fpreco,Produtos.fquantidade));
                Produtos.fpreco = "10,99";
                Produtos.fproduto  = "2";
                Produtos.fquantidade = "4";
                arraylistpedidos.add(new CarregarProdutosPedido(Produtos.fproduto,Produtos.fpreco,Produtos.fquantidade));
                Produtos.fpreco = "10,99";
                Produtos.fproduto  = "4";
                Produtos.fquantidade = "6";
                arraylistpedidos.add(new CarregarProdutosPedido(Produtos.fproduto,Produtos.fpreco,Produtos.fquantidade));*/

            }
        });

        binding.btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdapterPedidos adapterPedidos = new AdapterPedidos(requireContext(),arraylistpedidos);

                edtnomecliente = binding.editTextNomeCliente.getText().toString();
                edtcanal = binding.editTextCanal.getText().toString();
                edtcodfuncionarios = binding.editTextCodFuncionario.getText().toString();
                //edtforma = binding.editTextFormadepagamento.getText().toString();

                //CarregarProdutosPedido Produtos = new CarregarProdutosPedido();


                for (int i = 0; i < adapterPedidos.getCount(); i++) {
                    // Get row's spinner
                    //String listItem = binding.ListViewPedidos.getItemAtPosition(i).toString();
                     /* Map<String, String> map = (Map<String, String>) binding.ListViewPedidos.getItemAtPosition(i);
                    String filename = map.get("fproduto");*/

                    lproduto = adapterPedidos.getItem(i).getProduto().toString();
                    lpreco = adapterPedidos.getItem(i).getpreco().toString();
                    lqtde = adapterPedidos.getItem(i).getQuantidade().toString();

                    lprodutoint = Integer.parseInt(lproduto);
                    lprecodouble = Double.parseDouble(lpreco);
                    lqtdeint = Integer.parseInt(lqtde);

                    temp.addProperty("produto",lprodutoint);
                    temp.addProperty("preco",lprecodouble);
                    temp.addProperty("qtde",lqtdeint);
                    resultadolista = temp.toString();

                }

                apiCadastrar();

               /* if (edtnomecliente.isEmpty() || edtcanal.isEmpty() || edtcodfuncionarios.isEmpty() || edtforma.isEmpty() || edtcodproduto.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                            .setTitle("Erro")
                            .setMessage("Favor preencher os campos")
                            .setPositiveButton("OK",null);
                    builder.create().show();
                }
                else {

                        try {
                            JSONObject lstObj = new JSONObject();
                            lstObj.put("produto", binding.ListViewPedidos);
                           // lstObj.put("qtde", carregarProdutosPedido.getQuantidade());
                           // lstObj.put("preco", carregarProdutosPedido.getpreco());

                            for (int i = 0; i < binding.ListViewPedidos.getCount(); i++) {


                            }
                        }
                        catch (JSONException ex){
                            ex.printStackTrace();

                        }


                    if (limparcampo == 1) {
                        binding.editTextNomeCliente.setText("");
                        binding.editTextCanal.setText("");
                        binding.editTextCodFuncionario.setText("");
                        binding.editTextFormadepagamento.setText("");
                        binding.editTextCodProduto.setText("");
                    }
                }*/
            }
        });

    }

    protected void apiCadastrar() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtnomecliente", edtnomecliente.toString())
                .addBodyParameter("txtcanal", edtcanal.toString())
                .addBodyParameter("txtforma", formadepagaento.toString())
                .addBodyParameter("txtcodfuncionario", edtcodfuncionarios.toString())
                .addBodyParameter("txtdpgto", datapande.toString())
                .addBodyParameter("txtdenvio", datapande.toString())
                .addBodyParameter("txtproduto", resultadolista.toString())

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {
                                if (jsonObject.getJSONArray("RetornoDados").getInt(Integer.parseInt("numeropedido")) != 0) {
                                    mensagem = "Pedido realizado com sucesso";
                                } else  {
                                    mensagem = "Não foi possivel realizar o pedido";

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

    protected void apiConsultaPreco() {
        AndroidNetworking.post(apiPathconsulta)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtcodprod",edtcodproduto.toString() )
                .addBodyParameter("txtdescricao", stringvazia.toString())
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

                                //arraylistpedidos = new ArrayList<CarregarProdutosPedido>();
                                    CarregarProdutosPedido Produtos = new CarregarProdutosPedido();
                                    Produtos.fquantidade = edtquantidade.toString();
                                    for (int i = 0; i < restulJsonArray.length(); i++) {
                                        jsonObj = restulJsonArray.getJSONObject(i);
                                        Produtos.fpreco = jsonObj.getString("preco");
                                        Produtos.fproduto  = jsonObj.getString("codProduto");

                                        arraylistpedidos.add(new CarregarProdutosPedido(Produtos.fproduto,Produtos.fpreco,Produtos.fquantidade));
                                    }
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