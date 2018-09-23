package com.github.kenyamoura.compreaqui.view.perfil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.databinding.ActivityPerfilBinding;
import com.github.kenyamoura.compreaqui.infraestrutura.BaseActivity;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;
import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarActivity;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

public class PerfilActivity extends BaseActivity implements PerfilViewCallback {

    private PerfilViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String emailClienteAutenticado = recuperarEmailClienteSessao();
        if (isBlank(emailClienteAutenticado)) {
            abrirTela(AutenticarActivity.class);
            finish();
        }

        this.viewModel = DependenciasFactory.criarPerfilViewModel(this, getResources().getStringArray(R.array.estados_array));
        ActivityPerfilBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfil);
        dataBinding.setViewModel(viewModel);

        carregarPerfil(emailClienteAutenticado);
    }

    private void carregarPerfil(String email) {
        exibirPopupCarregando();
        viewModel.carregarPerfil(email);
    }

    private String recuperarEmailClienteSessao() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString("emailClienteAutenticado", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apagar_conta:
                confirmarApagarConta();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        removerEmailClienteSessao();
        abrirTela(AutenticarActivity.class);
        finish();
    }

    private void removerEmailClienteSessao() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void confirmarApagarConta() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmação")
                .setMessage("Deseja realmente apagar a conta?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.apagar_conta, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        destruirConta();
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void destruirConta() {
        exibirPopup(R.string.apagando_conta);
        viewModel.destruirConta();
    }

    @Override
    public void onErro(Throwable erro) {
        pararPopup();
        exibirMensagemRapida(erro.getMessage());
    }

    @Override
    public void onPerfilCarregado() {
        pararPopup();
    }

    public void onSalvarClick(View view) {
        exibirPopup(R.string.salvando);
        viewModel.salvarPerfil();
    }

    @Override
    public void onPerfilSalvoComSucesso() {
        pararPopup();
        exibirMensagemRapida(getString(R.string.perfil_salvo_com_sucesso));
    }

    @Override
    public void onPerfilDestruido() {
        pararPopup();
        logout();
    }

    @Override
    protected void onStop() {
        viewModel.onFinalizar();
        super.onStop();
    }
}
