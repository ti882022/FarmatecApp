package com.farmatecalphaversion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.farmatecalphaversion.CarregarProdutosPedido;
import com.farmatecalphaversion.R;

import java.util.ArrayList;

public class AdapterPedidos extends ArrayAdapter<CarregarProdutosPedido> {
    public AdapterPedidos(@NonNull Context context, ArrayList<CarregarProdutosPedido> arrayList){
        super(context,0,arrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        View currenItemView = convertView;
        //da visualização reciclável é nula, em seguida, inflar o layout personalizado para o mesmo
        if (currenItemView == null){
            currenItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view,parent,false);
        }

        //obter a posição da visão do ArrayAdapter
        CarregarProdutosPedido currentCarregarProdutosPedido = getItem(position);


        TextView textView1 = currenItemView.findViewById(R.id.textViewProduto);
        textView1.setText(currentCarregarProdutosPedido.getProduto());

        TextView textView2 = currenItemView.findViewById(R.id.textViewPreco);
        textView2.setText(currentCarregarProdutosPedido.getpreco());

        TextView textView3 = currenItemView.findViewById(R.id.textViewQuantidade);
        textView3.setText(currentCarregarProdutosPedido.getQuantidade());

        return currenItemView;
    }
}
