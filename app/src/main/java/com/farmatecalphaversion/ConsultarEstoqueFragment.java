package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

//import com.farmatecalphaversion.databinding.FragmentConsultarEstoqueBinding;

public class ConsultarEstoqueFragment extends Fragment {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/produtos/listar/";
    private JSONArray restulJsonArray;
    private SharedViewMode sharedViewModel;
    String categoriarecebida ="",conteudo="teste";
    private TextView barradepesquisa;
    private String mensagem = "", categorias = " " ,stringvazia = "";
    Button btnConsultar;


    private ConsultarEstoqueViewModel mViewModel;
    //private FragmentConsultarEstoqueBinding binding;
    static ArrayList<String> arrayListEstoque = new ArrayList<>();


    public static ConsultarEstoqueFragment newInstance() {
        return new ConsultarEstoqueFragment();
    }

    public ConsultarEstoqueFragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consultar_estoque, container, false);
        //binding = FragmentConsultarEstoqueBinding.inflate(inflater,container,false);
       // View root = binding.getRoot();

        //sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewMode.class);
        //sharedViewModel.getNameData().observe(getActivity(), nameObserver);
        btnConsultar = view.findViewById(R.id.btnconsultarestoquefragmet);
        barradepesquisa = view.findViewById(R.id.txtvBarradePesquisa);
        return view;
    }

    Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onChanged(String name) {
            categoriarecebida = name;
        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        ListView listView = view.findViewById(R.id.lstviewConsultarEstoque);
        ArrayAdapter ListAdapterCateorias = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayListEstoque);
        listView.setAdapter(ListAdapterCateorias);
        ListAdapterCateorias.clear();



        /*if (categoriarecebida != null) {
            barradepesquisa.setText(categoriarecebida);
            ListAdapterCateorias.clear();
        }*/
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ListAdapterCateorias == null){
                    CategoriasApi();
                }
                else{
                    ListAdapterCateorias.clear();
                    CategoriasApi();
                }


               /* if(ListAdapterCateorias != null){
                    ListAdapterCateorias.clear();
                    CategoriasApi();
                    ArrayAdapter ListAdapterCateorias = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListEstoque);
                    listView.setAdapter(ListAdapterCateorias);
                }
               else if( ListAdapterCateorias == null){
                    CategoriasApi();
                    ArrayAdapter ListAdapterCateorias = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListEstoque);
                    listView.setAdapter(ListAdapterCateorias);
                }*/

            }
        });

    }
    protected void CategoriasApi() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtcategoria", barradepesquisa.getText().toString())
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
                                    arrayListEstoque.add(jsonObj.getString("codProduto")+" / "+jsonObj.getString("descricao")+" / "+ jsonObj.getString("qtde"));


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