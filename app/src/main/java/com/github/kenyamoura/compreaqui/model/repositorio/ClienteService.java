package com.github.kenyamoura.compreaqui.model.repositorio;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface utilizada pelo Retrofit para mapear chamadas ao Backend.
 */
public interface ClienteService {

    /**
     * Método utilizado para autenticar assíncronamente.
     *
     * @param credenciais Credenciais do usuário com email e senha.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    @POST("/clientes/autenticar")
    Observable<Cliente> autenticar(@Body Credenciais credenciais);

    /**
     * Método utilizado para recuperar o cliente assíncronamente.
     *
     * @param email O e-mail do cliente.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    @GET("/clientes/{email}")
    Observable<Cliente> buscar(@Path("email") String email);

    /**
     * Método utilizado para criar um novo cliente assíncronamente.
     *
     * @param novoCliente Informações no novo cliente
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    @POST("/clientes/")
    Observable<Cliente> cadastrar(@Body Cliente novoCliente);

    /**
     * Método utilizado para atualizar um cliente assíncronamente.
     *
     * @param email   O e-mail pré-cadastrado do cliente.
     * @param cliente As novas informações do cliente.
     * @return Retorna um objeto observável do RxJava que pode retornar um cliente, um erro, ou vazio.
     */
    @PUT("/clientes/{email}")
    Observable<Cliente> atualizar(@Path("email") String email, @Body Cliente cliente);

    /**
     * Método utilizado para apagar a conta do cliente assíncronamente.
     *
     * @param email O e-mail do cliente que deseja-se apagar.
     * @return Retorna um objeto observável do RxJava que pode retornar um erro ou vazio.
     */
    @DELETE("/clientes/{email}")
    Completable apagar(@Path("email") String email);
}
