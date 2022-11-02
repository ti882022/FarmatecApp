package com.farmatecalphaversion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.farmatecalphaversion.databinding.ActivityBaseMenuBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class BaseMenu extends AppCompatActivity {
    private String apiPath = "http://10.38.45.24:8080/farmatec-api/usuarios/desconectar/";
    private JSONArray restulJsonArray;
    private int logado = 0;
    private String mensagem = "", titulo;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityBaseMenuBinding binding;
    private TextView txtnome, txtemail, txtnomeuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBaseMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarBaseMenu.toolbar);
        binding.appBarBaseMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        //referencia ao cabeçalho do menu
        View headerview = navigationView.getHeaderView(0);
        txtnome = headerview.findViewById(R.id.textViewNomeCompleto);
        txtemail = headerview.findViewById(R.id.textViewEmailUsuario);
        txtnomeuser = headerview.findViewById(R.id.textViewNameUsuario);

        //Recebe os dados do LoginActivity.
        Intent login = getIntent();
        txtnomeuser.setText(String.valueOf(login.getStringExtra("usuario")));
        txtnome.setText(String.valueOf(login.getStringExtra("nomecompleto")));
        txtemail.setText(String.valueOf(login.getStringExtra("email")));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menu)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_base_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnlogout) {
                    titulo = "Aviso";
                    mensagem = "Deseja realmente sair?";
                    final View dialog = getLayoutInflater().inflate(R.layout.activity_login,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(BaseMenu.this)
                            .setTitle(titulo)
                            .setMessage(mensagem)
                            .setNegativeButton("Não", null)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logoutApi();
                                }
                            });
                    builder.create().show();
                }
                return false;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_base_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    protected void logoutApi(){
        AndroidNetworking.post(apiPath)
                .addBodyParameter("HTTP_ACCEPT", "application/Json")
                .addBodyParameter("txtnome", txtnome.getText().toString())
                .addBodyParameter("txtemail", txtemail.getText().toString())
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
                                logado = jsonObj.getInt("plogado");
                                if  (logado == 1){
                                    Intent login = new Intent(getApplicationContext(),
                                            Login.class);
                                    startActivity(login);
                                    finish();

                                }

                            }
                        }

                        catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            if (anError.getErrorCode() == 0){
                                mensagem = "Problemas com conexão!! \nTente novamente.";
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(BaseMenu.this)
                                    .setTitle("Aviso")
                                    .setMessage(mensagem).
                                    setPositiveButton("ok", null);
                            builder.create().show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d("BridgeUpdateService", "error" + anError.getErrorCode() + anError.getErrorDetail());

                    }
                });
}
}