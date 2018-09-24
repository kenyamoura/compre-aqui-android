package com.github.kenyamoura.compreaqui.view.autenticar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.databinding.ActivityAutenticarBinding;
import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.infraestrutura.BaseActivity;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;
import com.github.kenyamoura.compreaqui.view.cadastro.CadastroActivity;
import com.github.kenyamoura.compreaqui.view.perfil.PerfilActivity;

/*
 * AutenticarActivity lida com ações de autenticação.
 * Redireciona para tela de criação de contas ou perfil.
 */
public class AutenticarActivity extends BaseActivity implements AutenticarViewCallback {

    // Código de retorno para CadastroActivity
    private static final int USUARIO_CADASTRADO_COM_SUCESSO = 201;

    private AutenticarViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cria viewModel utilizando uma fábrica
        this.viewModel = DependenciasFactory.criarAutenticarViewModel(this);
        // Carrega o layout e define as variáveis do mesmo
        ActivityAutenticarBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_autenticar);
        dataBinding.setViewModel(viewModel);
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

    /*
     * Evento chamado quando o usuário clica no botão "Login".
     * Requisitamos ao viewModel para realizar login.
     */
    public void onLoginClick(View view) {
        viewModel.onLoginClick();
    }

    /**
     * Evento de callback utilizada pela viewModel ao autenticar com sucesso.
     * Para qualquer popup, grava na sessão e abre a tela de perfil.
     *
     * @param cliente O cliente autenticado.
     */
    @Override
    public void onUsuarioAutenticado(Cliente cliente) {
        pararPopup();
        gravarEmailClienteNaSessao(cliente.getEmail());
        abrirTela(PerfilActivity.class);
        finish();
    }

    /**
     * Caso ocorra qualquer erro, esse método será chamado pelo viewModel.
     * Mostra uma mensagem rápida com o texto "erro" e para popups.
     *
     * @param erro O erro causado.
     */
    @Override
    public void onErroAoAutenticar(Throwable erro) {
        pararPopup();
        exibirErroRapido(erro.getMessage());
    }

    /**
     * Método utilizado para guardar o e-mail do usuário autenticado/cadastrado na sessão.
     * Essa informação é necessária para ser utilizada na PerfilActivity.
     *
     * @param email Email que será salvo na sessão.
     */
    private void gravarEmailClienteNaSessao(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailClienteAutenticado", email);
        editor.apply();
    }


    /*
     * Caso o usuário não possua conta, ele clicará no link para tal.
     * Esse método será chamado, esperando um retorno de sucesso da CadastroActivity,
     * que será tratado no método "onActivityResult" abaixo.
     */
    public void onCriarContaClick(View view) {
        abrirTelaEsperandoResultado(CadastroActivity.class, USUARIO_CADASTRADO_COM_SUCESSO);
    }

    /*
     * Trata o caso do usuário se cadastrar com sucesso.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == USUARIO_CADASTRADO_COM_SUCESSO && resultCode == Activity.RESULT_OK && data != null) {
            // grava o email recém criado na sessão
            gravarEmailClienteNaSessao(data.getStringExtra("emailCadastrado"));
            // abre a tela de perfil
            abrirTela(PerfilActivity.class);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
