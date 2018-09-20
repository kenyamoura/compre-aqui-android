package com.github.kenyamoura.compreaqui.view.autenticar;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;

import io.reactivex.Single;
import io.reactivex.observers.DisposableMaybeObserver;

public class AutenticarViewModel {

    private ClienteRepositorio clienteRepositorio;
    private AutenticarViewCallback viewCallback;

    // observables usados na activity
    public ObservableField<String> emailField = new ObservableField<>();
    public ObservableField<String> senhaField = new ObservableField<>();
    public ObservableBoolean estadoCarregando = new ObservableBoolean();


    public AutenticarViewModel(ClienteRepositorio clienteRepositorio, AutenticarViewCallback viewCallback) {
        this.clienteRepositorio = clienteRepositorio;
        this.viewCallback = viewCallback;
    }

    public void onLoginClick() {
        String email = emailField.get();
        String senha = senhaField.get();

        if (isBlank(email) || isBlank(senha)) {
            // TODO: Adicionar validacao
            return;
        }

        estadoCarregando.set(true);
        Credenciais credenciais = new Credenciais(email, senha);
        clienteRepositorio.autenticar(credenciais)
                .doOnSuccess(cliente -> viewCallback.onUsuarioAutenticado(cliente))
                .doOnError(throwable -> viewCallback.onErroAoAutenticar())
                .subscribe();;
    }

    private boolean isBlank(String campo) {
        return campo == null || campo.trim().isEmpty();
    }
}
