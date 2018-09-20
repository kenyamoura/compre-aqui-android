package com.github.kenyamoura.compreaqui.infraestrutura;

import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarViewCallback;
import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarViewModel;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DependenciasFactory {

    private static final String HOST = "https://compre-aqui.herokuapp.com/";

    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static AutenticarViewModel criarAutenticarViewModel(AutenticarViewCallback viewCallback) {
        return new AutenticarViewModel(criarClienteRepositorio(), viewCallback);
    }

    private static ClienteRepositorio criarClienteRepositorio() {
        return new ClienteRepositorio(criarClienteService());
    }

    private static ClienteService criarClienteService() {
        return retrofit.create(ClienteService.class);
    }
}
