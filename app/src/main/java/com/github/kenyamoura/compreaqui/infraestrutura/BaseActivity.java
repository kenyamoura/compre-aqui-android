package com.github.kenyamoura.compreaqui.infraestrutura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.kenyamoura.compreaqui.R;

/**
 * BaseActivity que implementa métodos comumente utilizados pelas Activities do sistema.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog carregandoDialog;

    /**
     * Exibe um popup de progresso com o título "Carregando..." e subtitulo "Aguarde"
     */
    protected void exibirPopupCarregando() {
        exibirPopup(R.string.carregando);
    }

    /**
     * Exibe um popup de progresso com título customizável e subtitulo "Aguarde"
     *
     * @param titulo Titulo para exibir no popup.
     */
    protected void exibirPopup(@StringRes int titulo) {
        exibirPopup(titulo, R.string.aguarde);
    }

    /**
     * Exibe um popup de progresso com título e subtitulo customizáveis.
     * Para popups que estejam sendo exibidos no momento da chamada.
     *
     * @param titulo   Titulo para exibir no popup.
     * @param mensagem Mensagem para exibir abaixo do título.
     */
    protected void exibirPopup(@StringRes int titulo, @StringRes int mensagem) {
        pararPopup();
        carregandoDialog = ProgressDialog.show(
                BaseActivity.this,
                getString(titulo),
                getString(mensagem),
                true,
                false);
    }

    /**
     * Para qualquer popup que esteja sendo exibido.
     */
    protected void pararPopup() {
        if (carregandoDialog != null) {
            carregandoDialog.dismiss();
            carregandoDialog = null;
        }
    }

    /**
     * Exibe uma mensagem rapidamente na tela.
     *
     * @param mensagem Mensagem que deseja exibir.
     */
    protected void exibirMensagemRapida(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    /**
     * Exibe uma mensagem de erro rapidamente na tela com o texto "Erro" no começo.
     *
     * @param erro Mensagem de erro que deseja exibir.
     */
    protected void exibirErroRapido(String erro) {
        exibirMensagemRapida(getString(R.string.erro) + " " + erro);
    }

    /**
     * Abre uma nova activity.
     *
     * @param clazz A classe da Activity que deseja abrir.
     */
    protected void abrirTela(Class<?> clazz) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        startActivity(intent);
    }

    /**
     * Abre uma nova activity aguardando um resultado específico.
     *
     * @param clazz           A classe da Activity que deseja abrir.
     * @param codigoResultado O código que a activity que chamou receberá.
     */
    public void abrirTelaEsperandoResultado(Class<?> clazz, int codigoResultado) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        startActivityForResult(intent, codigoResultado);
    }
}
