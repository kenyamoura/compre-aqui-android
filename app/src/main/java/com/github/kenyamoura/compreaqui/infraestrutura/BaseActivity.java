package com.github.kenyamoura.compreaqui.infraestrutura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.kenyamoura.compreaqui.R;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog carregandoDialog;

    protected ProgressDialog exibirPopupCarregando() {
        return exibirPopup(R.string.carregando);
    }

    protected ProgressDialog exibirPopup(@StringRes int titulo) {
        return exibirPopup(titulo, R.string.aguarde);
    }

    protected ProgressDialog exibirPopup(@StringRes int titulo, @StringRes int mensagem) {
        pararPopup();
        carregandoDialog = ProgressDialog.show(
                BaseActivity.this,
                getString(titulo),
                getString(mensagem),
                true,
                false);
        return carregandoDialog;
    }

    protected void pararPopup() {
        if (carregandoDialog != null) {
            carregandoDialog.dismiss();
            carregandoDialog = null;
        }
    }

    protected void exibirMensagemRapida(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    protected void abrirTela(Class<?> clazz) {
        Intent intent = new Intent(BaseActivity.this, clazz);
        startActivity(intent);
    }
}
