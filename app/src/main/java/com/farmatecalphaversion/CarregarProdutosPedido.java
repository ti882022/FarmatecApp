package com.farmatecalphaversion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CarregarProdutosPedido {

    public String fproduto;
    public String fpreco;
    public String fquantidade;

    /*
    public CarregarProdutosPedido(){}
    public CarregarProdutosPedido(int produto, String preco, int quantidade ){
        this.produto = produto;
        this.preco = preco;
        this.quantidade = quantidade;
    }
        public int getProduto(){
            return produto;
        }
        public void setProduto(int produto){
            this.produto = produto;
        }
        public String getpreco(){
            return preco;
        }
        public void setpreco(String preco){
            this.preco = preco;
        }
        public int getQuantidade(){
            return quantidade;
        }
        public void setQuantidade(int quantidade){
            this.quantidade = quantidade;
        }*/
    public CarregarProdutosPedido(String produto, String preco, String quantidade ){
        fproduto = produto;
        fpreco = preco;
        fquantidade = quantidade;
    }

    public CarregarProdutosPedido() {

    }

    public String getProduto(){

        return fproduto;
    }

    public String getpreco(){
        return fpreco;
    }

    public String getQuantidade(){
        return fquantidade;
    }



}