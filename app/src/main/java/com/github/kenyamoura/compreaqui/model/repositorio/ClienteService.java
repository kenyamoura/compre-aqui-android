package com.github.kenyamoura.compreaqui.model.repositorio;

import com.github.kenyamoura.compreaqui.dominio.Cliente;
import com.github.kenyamoura.compreaqui.dominio.Credenciais;

import io.reactivex.Observable;
import retrofit2.Call;
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
    Call<Cliente> buscar(@Path("email") String email);

    @POST("/clientes/")
    Call<Cliente> buscar(@Body Cliente novoCliente);

    @PUT("/clientes/{email}")
    Call<Cliente> atualizar(@Path("email") String email, @Body Cliente cliente);

    @DELETE("/clientes/{email}")
    Call<Void> apagar(@Path("email") String email);
}
