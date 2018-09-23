package com.github.kenyamoura.compreaqui.view.autenticar;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.databinding.ActivityAutenticarBinding;
import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.infraestrutura.BaseActivity;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;
import com.github.kenyamoura.compreaqui.view.cadastro.CadastroActivity;
import com.github.kenyamoura.compreaqui.view.perfil.PerfilActivity;

public class AutenticarActivity extends BaseActivity implements AutenticarViewCallback {

    private AutenticarViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = DependenciasFactory.criarAutenticarViewModel(this);
        ActivityAutenticarBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_autenticar);
        dataBinding.setViewModel(viewModel);
    }

    @Override
    protected void onStop() {
        pararPopup();
        viewModel.onFinalizar();
        super.onStop();
    }

    public void onLoginClick(View view) {
        viewModel.onLoginClick();
    }

    @Override
    public void onUsuarioAutenticado(Cliente cliente) {
        pararPopup();
        gravarEmailClienteNaSessao(cliente.getEmail());
        abrirTela(PerfilActivity.class);
        finish();
    }

    private void gravarEmailClienteNaSessao(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailClienteAutenticado", email);
        editor.apply();
    }

    @Override
    public void onErroAoAutenticar(Throwable e) {
        pararPopup();
        exibirMensagemRapida(getString(R.string.erro)  + " " + e.getMessage());
    }

    public void onCriarContaClick(View view) {
        abrirTela(CadastroActivity.class);
    }

}
