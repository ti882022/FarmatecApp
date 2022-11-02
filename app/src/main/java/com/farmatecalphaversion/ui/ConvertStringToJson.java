package com.farmatecalphaversion.ui;


import com.farmatecalphaversion.CarregarProdutosPedido;

import org.json.JSONException;
import org.json.JSONObject;

public class ConvertStringToJson {
    public static String toJson(CarregarProdutosPedido carregarProdutosPedido){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("produto", carregarProdutosPedido.getProduto());
            jsonObject.put("qtde", carregarProdutosPedido.getQuantidade());
            jsonObject.put("preco", carregarProdutosPedido.getpreco());


        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;

    }

}
