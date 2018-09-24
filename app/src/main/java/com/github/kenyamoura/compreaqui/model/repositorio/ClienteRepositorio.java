package com.github.kenyamoura.compreaqui.model.repositorio;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import retrofit2.HttpException;


/**
 * Repositório com ações de acesso aos registros do cliente.
 */
public class ClienteRepositorio {

    // Dependência da classe.
    private ClienteService clienteService;

    public ClienteRepositorio(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Método utilizado para autenticar assíncronamente.
     *
     * @param credenciais Credenciais do usuário com email e senha.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    public Maybe<Cliente> autenticar(Credenciais credenciais) {
        return clienteService.autenticar(credenciais)
                .onErrorResumeNext(e -> {
                    /*
                     * Trata erro 403, que ocorre quando ou e-mail ou senha está errada.
                     */
                    if (e instanceof HttpException && ((HttpException) e).code() == 403)
                        return Observable.error(new RuntimeException("Email e/ou senha inválidos."));
                    else
                        return Observable.error(e);
                })
                .singleElement(); // Requisita apenas um elemento.
    }

    /**
     * Método utilizado para criar um novo cliente assíncronamente.
     *
     * @param novoCliente Informações no novo cliente
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    public Maybe<Cliente> cadastrar(Cliente novoCliente) {
        return clienteService.cadastrar(novoCliente)
                .singleElement();
    }

    /**
     * Método utilizado para recuperar o cliente assíncronamente.
     *
     * @param email O e-mail do cliente.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    public Maybe<Cliente> buscar(String email) {
        return clienteService.buscar(email)
                .singleElement();
    }

    /**
     * Método utilizado para atualizar um cliente assíncronamente.
     *
     * @param email   O e-mail pré-cadastrado do cliente.
     * @param cliente As novas informações do cliente.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    public Maybe<Cliente> atualizar(String email, Cliente cliente) {
        return clienteService.atualizar(email, cliente)
                .singleElement();
    }

    /**
     * Método utilizado para apagar a conta do cliente assíncronamente.
     *
     * @param email O e-mail do cliente que deseja-se apagar.
     * @return Retorna um objeto observável do RxJava que pode retornar um erro ou vazio.
     */
    public Completable destruirConta(String email) {
        return clienteService.apagar(email);
    }
}
