package com.github.kenyamoura.compreaqui.view.autenticar;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

import static com.github.kenyamoura.compreaqui.utils.StringUtils.isBlank;

public class AutenticarViewModel {

    private ClienteRepositorio clienteRepositorio;
    private AutenticarViewCallback viewCallback;

    // observables usados na activity
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AutenticarViewModel(ClienteRepositorio clienteRepositorio, AutenticarViewCallback viewCallback) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
    }

    public void onLoginClick() {
        String email = emailField.get();
        String senha = senhaField.get();

        if (isBlank(email) || isBlank(senha)) {
            viewCallback.onErroAoAutenticar(new RuntimeException("Preencha login e senha."));
            return;
        }

        Credenciais credenciais = new Credenciais(email, senha);
        Disposable disposable = clienteRepositorio.autenticar(credenciais)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> viewCallback.onUsuarioAutenticado(null))
                .subscribe(
                        viewCallback::onUsuarioAutenticado,
                        viewCallback::onErroAoAutenticar);
        compositeDisposable.add(disposable);
    }

    public void onFinalizar() {
        compositeDisposable.clear();
    }
}
