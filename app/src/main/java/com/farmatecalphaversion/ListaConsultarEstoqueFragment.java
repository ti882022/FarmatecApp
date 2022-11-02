package com.farmatecalphaversion;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.farmatecalphaversion.ListaConsultarEstoqueFragment;
import com.farmatecalphaversion.databinding.FragmentConsultarEstoqueBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ListaConsultarEstoqueFragment extends Fragment {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/produtos/listar/";
    private JSONArray restulJsonArray;
    private String mensagem = "", categorias = " " ,conteudo="";
    Button btnConsultar;    //Config Caixa de texto
    private EditText editText;
    private SharedViewMode sharedViewModel;
    static ArrayList<String> arrayListCateorias = new ArrayList<>();
    
public ListaConsultarEstoqueFragment(){

}

    private ListaConsultarEstoqueViewModel mViewModel;
    //private FragmentConsultarEstoqueBinding binding;

    public static ListaConsultarEstoqueFragment newInstance() {return new ListaConsultarEstoqueFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_consultar_estoque, container, false);

        editText = view.findViewById(R.id.edtBarradePesquisa);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        ListView listView = view.findViewById(R.id.lstviewConsultarProdutos);

        ArrayAdapter ListAdapterCateorias = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayListCateorias);

        listView.setAdapter(ListAdapterCateorias);


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListAdapterCateorias == null){
                    CategoriasApi();
                }
                else{
                    ListAdapterCateorias.clear();
                    CategoriasApi();

                }
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ListAdapterCateorias.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewMode.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Navigation.findNavController(view).navigate(R.id.nav_ConsulatrEstoque);
                sharedViewModel.setNameData(ListAdapterCateorias.getItem(position).toString());
                ListAdapterCateorias.clear();


            }
        });
    }
    protected void CategoriasApi() {
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtcategoria", categorias.toString())
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
                                    //categorias = jsonObj.getString("categoria");
                                    arrayListCateorias.add(jsonObj.getString("categoria"));


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

    /*@Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }*/

}