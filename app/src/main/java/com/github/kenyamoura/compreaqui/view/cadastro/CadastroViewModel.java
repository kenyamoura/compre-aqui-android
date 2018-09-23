package com.github.kenyamoura.compreaqui.view.cadastro;

import android.databinding.ObservableField;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

public class CadastroViewModel {

    // observables usados na activity
    public ObservableField<String> nomeField = new ObservableField<>();
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();
    public ObservableField<String> confirmarSenhaField = new ObservableField<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final ClienteRepositorio clienteRepositorio;
    private final CadastroViewCallback viewCallback;

    public CadastroViewModel(ClienteRepositorio clienteRepositorio, CadastroViewCallback viewCallback) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
    }

    public void onRegistrarClick() {
        String email = emailField.get();
        if (isBlank(email)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("Preencha o email."));
            return;
        }
        String senha = senhaField.get();
        if (isBlank(senha)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("Preencha a senha."));
            return;
        }
        String confirmacaoSenha = confirmarSenhaField.get();
        if (isBlank(confirmacaoSenha) || !senha.equals(confirmacaoSenha)) {
            viewCallback.onErroAoCadastrar(new RuntimeException("A confirmação de senha deve preenchida igual a senha."));
            return;
        }
        String nome = nomeField.get();

        Cliente novoCliente = new Cliente(email, senha, null, nome, null, null, null, null);
        Disposable subscribe = clienteRepositorio.cadastrar(novoCliente)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(viewCallback::onClienteCadastradoComSucesso,
                        viewCallback::onErroAoCadastrar);
        compositeDisposable.add(subscribe);
    }

    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
