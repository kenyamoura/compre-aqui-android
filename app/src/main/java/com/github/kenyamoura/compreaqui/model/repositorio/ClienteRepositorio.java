package com.github.kenyamoura.compreaqui.model.repositorio;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;

import io.reactivex.Maybe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ClienteRepositorio {

    private ClienteService clienteService;

    public ClienteRepositorio(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    public Maybe<Cliente> autenticar(Credenciais credenciais) {
        return clienteService.autenticar(credenciais)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(this::handleLoginError)
                .singleElement();
    }


    private void handleLoginError(Observer<? super Cliente> observer) {
        observer.onError(null);
    }
}
