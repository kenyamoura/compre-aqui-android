package com.github.kenyamoura.compreaqui.view.cadastro;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.databinding.ActivityCadastroBinding;
import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.infraestrutura.BaseActivity;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;

public class CadastroActivity extends BaseActivity implements CadastroViewCallback {

    private CadastroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = DependenciasFactory.criarCadastroViewModel(this);
        ActivityCadastroBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_cadastro);
        dataBinding.setViewModel(viewModel);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRegistrarClick(View view) {
        exibirPopup(R.string.salvando, R.string.aguarde);
        viewModel.onRegistrarClick();
    }

    @Override
    protected void onStop() {
        pararPopup();
        viewModel.onFinalizar();
        super.onStop();
    }

    @Override
    public void onClienteCadastradoComSucesso(Cliente cliente) {
        pararPopup();
        exibirMensagemRapida(getString(R.string.cadastro_sucesso) + " " + getString(R.string.faca_login));
        finish();
    }

    @Override
    public void onErroAoCadastrar(Throwable erro) {
        pararPopup();
        exibirMensagemRapida(erro.getMessage());
    }
}
