package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultarPedidosFragment extends Fragment {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/pedidos/listar/";
    private JSONArray restulJsonArray;
    private SharedViewMode sharedViewModel;
    String contentCPF="", contentCodProduto="";
    private TextView barradepesquisa;
    EditText edtCPF, edtCodproduto;
    private String mensagem = "", categorias = " " ,conteudo="",stringvazia = " ";
    Button btnlistar;

    private ConsultarPedidosViewModel mViewModel;
    static ArrayList<String> arrayListConsultarPedido = new ArrayList<>();
    public static ConsultarPedidosFragment newInstance() {
        return new ConsultarPedidosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_pedidos, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        edtCPF = (EditText) view.findViewById(R.id.editTextCPF);
        edtCodproduto = (EditText) view.findViewById(R.id.editTextCodproduto);
        btnlistar = view.findViewById(R.id.btnListarPedidos);

        ListView listView = view.findViewById(R.id.lstviewConsultarPedidos);

        ArrayAdapter ListAdapterCateorias = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayListConsultarPedido);
        listView.setAdapter(ListAdapterCateorias);
        ListAdapterCateorias.clear();

        btnlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListAdapterCateorias == null) {
                    CategoriasApi();
                }
                else{
                    ListAdapterCateorias.clear();
                    CategoriasApi();

                }
            }
        });


    }

    protected void CategoriasApi() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtnumpedido", edtCodproduto.getText().toString())
                .addBodyParameter("txtcpf", edtCPF.getText().toString())
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
                                for (int i = 0; i < restulJsonArray.length(); i++) {
                                    jsonObj = restulJsonArray.getJSONObject(i);
                                    if (edtCodproduto.getText().length()==0){
                                        arrayListConsultarPedido.add("N°Pedido: "+jsonObj.getString("nPedido") + " / Data de Pedido: " + jsonObj.getString("dataPedido") );

                                    }
                                    else {
                                        arrayListConsultarPedido.add("Data de Pedido: " + jsonObj.getString("dataPedido") + " / Data de Pagamento: " + jsonObj.getString("dataPgto") + " / Descrição: " + jsonObj.getString("descricao")+ " / Quantidade: " + jsonObj.getString("qtde") + " / Preco: " + jsonObj.getString("preco"));

                                    }


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
                                mensagem = "Problemas com conexão!! \nTente novamente.";
                            } else {
                                JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                                if (jsonObject.getJSONObject("RetornoDados").getInt("sucesso") == 0) {
                                    mensagem = "Usuário ou senha inválidos";
                                }
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
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