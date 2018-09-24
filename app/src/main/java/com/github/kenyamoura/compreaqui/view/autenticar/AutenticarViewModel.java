package com.github.kenyamoura.compreaqui.view.autenticar;

import android.databinding.ObservableField;

import com.github.kenyamoura.compreaqui.dominio.Credenciais;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

/*
 * ViewModel da AutenticarActivity
 */
public class AutenticarViewModel {

    // dependências da classe
    private ClienteRepositorio clienteRepositorio;
    private AutenticarViewCallback viewCallback;

    // observables usados na activity
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();

    // objeto utilizado pelo rxJava para automaticamente se desfazer das chamadas.
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AutenticarViewModel(ClienteRepositorio clienteRepositorio, AutenticarViewCallback viewCallback) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
    }

    /*
     * Método chamado pela View ao tentar autenticar. Requer email e senha.
     */
    public void onLoginClick() {
        String email = emailField.get();
        String senha = senhaField.get();

        // Validamos se as informações obrigatórias foram preenchidas.
        if (isBlank(email) || isBlank(senha)) {
            // Caso contrário, retorna erro com mensagem descritiva e encerra a execução.
            viewCallback.onErroAoAutenticar(new RuntimeException("Preencha login e senha."));
            return;
        }

        // Passa um objeto credenciais para o repositório.
        Credenciais credenciais = new Credenciais(email, senha);
        Disposable disposable = clienteRepositorio.autenticar(credenciais)
                .observeOn(AndroidSchedulers.mainThread()) // observa o resultado na thread principal do Android
                .subscribeOn(Schedulers.io()) // espera o resultado na thread de IO
                .subscribe(
                        viewCallback::onUsuarioAutenticado, // se sucesso, chama o callback passando o cliente
                        viewCallback::onErroAoAutenticar); // se erro, chama o callback de erro
        compositeDisposable.add(disposable); // adiciona a chamada acima para ser recolhida posteriormente
    }

    /*
     * Método chamado pela View para que o ViewModel execute tarefas necessárias antes de fechar a tela.
     */
    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
