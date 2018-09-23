package com.github.kenyamoura.compreaqui.model.repositorio;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.maybe.MaybeEmpty;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

public class ClienteRepositorio {

    private ClienteService clienteService;

    public ClienteRepositorio(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    public Maybe<Cliente> autenticar(Credenciais credenciais) {
        return clienteService.autenticar(credenciais)
                .subscribeOn(io())
                .observeOn(mainThread())
                .onErrorResumeNext(e -> {
                    if (e instanceof HttpException && ((HttpException) e).code() == 403)
                        return Observable.error(new RuntimeException("Email e/ou senha inválidos."));
                    else
                        return Observable.error(e);
                })
                .singleElement();
    }

    public Maybe<Cliente> cadastrar(Cliente novoCliente) {
        return clienteService.cadastrar(novoCliente)
                .subscribeOn(io())
                .observeOn(mainThread())
                .onErrorResumeNext((Function<Throwable, ObservableSource<? extends Cliente>>) Observable::error)
                .singleElement();
    }

    public Maybe<Cliente> buscar(String email) {
        return clienteService.buscar(email)
                .subscribeOn(io())
                .observeOn(mainThread())
                .onErrorResumeNext((Function<Throwable, ObservableSource<? extends Cliente>>) Observable::error)
                .singleElement();
    }

    public Maybe<Cliente> atualizar(String email, Cliente cliente) {
        return clienteService.atualizar(email, cliente)
                .subscribeOn(io())
                .observeOn(mainThread())
                .onErrorResumeNext((Function<Throwable, ObservableSource<? extends Cliente>>) Observable::error)
                .singleElement();
    }

    public Completable destruirConta(String email) {
        return clienteService.apagar(email)
                .subscribeOn(io())
                .observeOn(mainThread());
    }
}
