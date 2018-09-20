package com.github.kenyamoura.compreaqui.view.autenticar;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;

public class AutenticarActivity extends AppCompatActivity implements AutenticarViewCallback {

    private AutenticarViewModel viewModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticar);
        this.viewModel = DependenciasFactory.criarAutenticarViewModel(this);
    }

    public void onLoginClick(View view) {
        progressDialog = ProgressDialog.show(AutenticarActivity.this, "Carregando...", "Aguarde.", true, false);
        viewModel.onLoginClick();
    }

    @Override
    public void onUsuarioAutenticado(Cliente cliente) {
        progressDialog.dismiss();
    }

    @Override
    public void onErroAoAutenticar() {
        progressDialog.dismiss();
    }
}
