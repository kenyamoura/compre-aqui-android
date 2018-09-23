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

public interface ClienteService {

    @POST("/clientes/autenticar")
    Observable<Cliente> autenticar(@Body Credenciais credenciais);

    @GET("/clientes/{email}")
    Observable<Cliente> buscar(@Path("email") String email);

    @POST("/clientes/")
    Observable<Cliente> cadastrar(@Body Cliente novoCliente);

    @PUT("/clientes/{email}")
    Observable<Cliente> atualizar(@Path("email") String email, @Body Cliente cliente);

    @DELETE("/clientes/{email}")
    Completable apagar(@Path("email") String email);
}
