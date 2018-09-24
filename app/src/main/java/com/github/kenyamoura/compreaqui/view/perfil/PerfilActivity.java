package com.github.kenyamoura.compreaqui.view.perfil;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.kenyamoura.compreaqui.R;
import com.github.kenyamoura.compreaqui.databinding.ActivityPerfilBinding;
import com.github.kenyamoura.compreaqui.infraestrutura.BaseActivity;
import com.github.kenyamoura.compreaqui.infraestrutura.DependenciasFactory;
import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarActivity;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

/*
 * Esta activity lida com ações de atualizar perfil, apagar conta e fazer logout.
 * Retorna sempre para a tela de autenticação.
 */
public class PerfilActivity extends BaseActivity implements PerfilViewCallback {

    private PerfilViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // verifica se o cliente ainda está autenticado
        String emailClienteAutenticado = recuperarEmailClienteSessao();
        // caso contrário, volta para tela de login
        if (isBlank(emailClienteAutenticado)) {
            abrirTela(AutenticarActivity.class);
            finish();
            return;
        }

        // Cria viewModel utilizando uma fábrica
        this.viewModel = DependenciasFactory.criarPerfilViewModel(this, getResources().getStringArray(R.array.estados_array));
        // Carrega o layout e define as variáveis do mesmo
        ActivityPerfilBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfil);
        dataBinding.setViewModel(viewModel);

        // carrega a tela com informações do perfil vindas do servidor
        carregarPerfil(emailClienteAutenticado);
    }

    /**
     * Exibe popup de carregando e solicita ViewModel que carregue as informações do perfil na tela.
     *
     * @param email Email que se deseja carregar.
     */
    private void carregarPerfil(String email) {
        exibirPopupCarregando();
        viewModel.carregarPerfil(email);
    }

    /*
     * O e-mail do usuário autenticado fica guardado nas preferências da app.
     * Recuperamos para poder confirmar que está autenticado. Se não estiver, returna nulo.
     */
    private String recuperarEmailClienteSessao() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString("emailClienteAutenticado", null);
    }

    /*
     * Carrega o menu com opções de "Apagar conta" e "Logout".
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Trata eventos de click da ActionBar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_apagar_conta: // Caso cliente tente apagar conta, pede-se confirmação.
                confirmarApagarConta();
                return true;
            case R.id.action_logout: // Caso cliente tente logout, retorna para tela de login.
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Método chamado quando usuário clica no menu e então em "Logout".
     * Remove o e-mail da sessão e reabre tela de autenticar, finalizando a PerfilActivity.
     */
    private void logout() {
        removerEmailClienteSessao();
        abrirTela(AutenticarActivity.class);
        finish();
    }

    /*
     * O e-mail do usuário autenticado fica guardado nas preferências da app.
     * Apagamos para fazer logout.
     */
    private void removerEmailClienteSessao() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /*
     * Método chamado ao tentar apagar a conta.
     * Exibe um alerta com botão "Não" e "Apagar conta".
     */
    private void confirmarApagarConta() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmacao)
                .setMessage(R.string.deseja_realmente_apagar_conta)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.apagar_conta, (dialog, whichButton) -> destruirConta()) // se o usuário desejar apagar conta, chama este método.
                .setNegativeButton(android.R.string.no, null) // Caso aperte em "Não", fecha o alerta e nada acontece.
                .show();
    }

    /*
     * Método chamado após confirmação de apagar conta.
     * Exibe popup e solicita ViewModel que destrua a conta.
     * Utilizamos o nome "destruir" para ficar impactante esta ação.
     */
    private void destruirConta() {
        exibirPopup(R.string.apagando_conta);
        viewModel.destruirConta();
    }

    /**
     * Caso ocorra qualquer erro, esse método será chamado pela ViewModel.
     * Mostra uma mensagem rápida com o texto "erro" e para popups.
     *
     * @param erro Erro que ocorreu
     */
    @Override
    public void onErro(Throwable erro) {
        pararPopup();
        exibirErroRapido(erro.getMessage());
    }

    /*
     * Evento de callback utilizado pela ViewModel para alertar a View que o perfil foi carregado.
     * Para popup de "Carregando".
     */
    @Override
    public void onPerfilCarregado() {
        pararPopup();
    }

    /*
     * Evento chamado quando o usuário clica no botão "Salvar" na tela.
     * Exibe popup com mensagem de "Salvando" e solicita ViewModel que salve o perfil.
     */
    public void onSalvarClick(View view) {
        exibirPopup(R.string.salvando);
        viewModel.salvarPerfil();
    }

    /*
     * Evento de callback utilizado pela ViewModel para alertar quando o perfil for salvo com sucesso.
     * Para popup de "Carregando" e exibe mensagem de sucesso.
     */
    @Override
    public void onPerfilSalvoComSucesso() {
        pararPopup();
        exibirMensagemRapida(getString(R.string.perfil_salvo_com_sucesso));
    }

    /*
     * Evento de callback utilizado pela ViewModel para alertar quando o perfil for destruido com sucesso.
     * Para popup de "Apagando conta" e faz logout da conta, retornando para tela de login.
     */
    @Override
    public void onPerfilDestruido() {
        pararPopup();
        logout();
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
}
