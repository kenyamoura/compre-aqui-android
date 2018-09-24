package com.github.kenyamoura.compreaqui.view.cadastro;

import android.app.Activity;
import android.content.Intent;
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

/*
 * Esta activity lida com ações de cadastro.
 * Retorna sempre para a tela de autenticação.
 */
public class CadastroActivity extends BaseActivity implements CadastroViewCallback {

    private CadastroViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cria viewModel utilizando uma fábrica
        this.viewModel = DependenciasFactory.criarCadastroViewModel(this);
        // Carrega o layout e define as variáveis do mesmo
        ActivityCadastroBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_cadastro);
        dataBinding.setViewModel(viewModel);
        // Exibe botão de voltar no topo esquerdo
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Trata eventos de click da ActionBar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Ao clicar no botão voltar
                NavUtils.navigateUpFromSameTask(this); // Retorna para tela de login.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Evento chamado ao clicar no botão "Registrar".
     * Exibe popup para que o usuário aguarde e repassa o evento para viewModel.
     */
    public void onRegistrarClick(View view) {
        exibirPopup(R.string.salvando, R.string.aguarde);
        viewModel.onRegistrarClick();
    }

    /*
     * Evento chamado pelo Android ao mudar de tela.
     * Se a tela entrar em repouso, paramos o popup e avisamos a viewModel para finalizar
     */
    @Override
    protected void onStop() {
        pararPopup();
        viewModel.onFinalizar();
        super.onStop();
    }

    /**
     * Evento de callback chamado pela ViewModel caso o cliente seja cadastrado com sucesso.
     * Para popup de carregando e mensagem de sucesso.
     * Retorna para activity que a chamou (AutenticarActivity) com o e-mail do usuário e resultado "OK".
     *
     * @param cliente Cliente recém cadastrado.
     */
    @Override
    public void onClienteCadastradoComSucesso(Cliente cliente) {
        pararPopup();
        exibirMensagemRapida(getString(R.string.cadastro_sucesso));

        // retorna o email do usuário recém cadastrado.
        Intent data = new Intent();
        data.putExtra("emailCadastrado", cliente.getEmail());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    /**
     * Caso ocorra qualquer erro, esse método será chamado pela ViewModel.
     * Mostra uma mensagem rápida com o texto "erro" e para popups.
     *
     * @param erro O erro que ocorreu
     */
    @Override
    public void onErroAoCadastrar(Throwable erro) {
        pararPopup();
        exibirErroRapido(erro.getMessage());
    }
}
