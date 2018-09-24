package com.github.kenyamoura.compreaqui.view.cadastro;

import android.databinding.ObservableField;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

/*
 * ViewModel do CadastroViewModel
 */
public class CadastroViewModel {

    // dependências da classe
    private final ClienteRepositorio clienteRepositorio;
    private final CadastroViewCallback viewCallback;

    // observables usados na activity
    public ObservableField<String> nomeField = new ObservableField<>();
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();
    public ObservableField<String> confirmarSenhaField = new ObservableField<>();

    // objeto utilizado pelo rxJava para automaticamente se desfazer das chamadas.
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public CadastroViewModel(ClienteRepositorio clienteRepositorio, CadastroViewCallback viewCallback) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
    }

    /*
     * Método chamado pela View ao tentar registrar-se. Requer email, senha e confirmação da senha.
     */
    public void onRegistrarClick() {

        // valida email
        String email = emailField.get();
        if (isBlank(email)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("Preencha o email."));
            return;
        }

        // valida senha
        String senha = senhaField.get();
        if (isBlank(senha)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("Preencha a senha."));
            return;
        }

        // valida confirmação é igual a senha
        String confirmacaoSenha = confirmarSenhaField.get();
        if (isBlank(confirmacaoSenha) || !senha.equals(confirmacaoSenha)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("A confirmação de senha deve preenchida igual a senha."));
            return;
        }
        String nome = nomeField.get();

        // passa um novo cliente com informações necessárias para o repositório.
        Cliente novoCliente = new Cliente(email, senha, null, nome, null, null, null, null);
        Disposable subscribe = clienteRepositorio.cadastrar(novoCliente)
                .observeOn(AndroidSchedulers.mainThread()) // observa o resultado na thread principal do Android
                .subscribeOn(Schedulers.io()) // espera o resultado na thread de IO
                .subscribe(viewCallback::onClienteCadastradoComSucesso,  // se sucesso, chama o callback passando o cliente recém cadastrado
                        viewCallback::onErroAoCadastrar); // se erro, chama callback repassando o erro.
        compositeDisposable.add(subscribe); // adiciona a chamada acima para ser recolhida posteriormente
    }

    /*
     * Método chamado pela View para que o ViewModel execute tarefas necessárias antes de fechar a tela.
     */
    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
