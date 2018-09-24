package com.github.kenyamoura.compreaqui.infraestrutura;

import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarViewCallback;
import com.github.kenyamoura.compreaqui.view.autenticar.AutenticarViewModel;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteRepositorio;
import com.github.kenyamoura.compreaqui.model.repositorio.ClienteService;
import com.github.kenyamoura.compreaqui.view.cadastro.CadastroActivity;
import com.github.kenyamoura.compreaqui.view.cadastro.CadastroViewCallback;
import com.github.kenyamoura.compreaqui.view.cadastro.CadastroViewModel;
import com.github.kenyamoura.compreaqui.view.perfil.PerfilActivity;
import com.github.kenyamoura.compreaqui.view.perfil.PerfilViewCallback;
import com.github.kenyamoura.compreaqui.view.perfil.PerfilViewModel;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Arrays.asList;

public class DependenciasFactory {

    private static final String HOST = "https://compre-aqui.herokuapp.com/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final ClienteRepositorio clienteRepositorio = criarClienteRepositorio();

    public static AutenticarViewModel criarAutenticarViewModel(AutenticarViewCallback viewCallback) {
        return new AutenticarViewModel(clienteRepositorio, viewCallback);
    }

    private static ClienteRepositorio criarClienteRepositorio() {
        return new ClienteRepositorio(criarClienteService());
    }

    private static ClienteService criarClienteService() {
        return retrofit.create(ClienteService.class);
    }

    public static CadastroViewModel criarCadastroViewModel(CadastroViewCallback viewCallback) {
        return new CadastroViewModel(clienteRepositorio, viewCallback);
    }

    public static PerfilViewModel criarPerfilViewModel(PerfilViewCallback viewCallback, String[] estados) {
        return new PerfilViewModel(clienteRepositorio, viewCallback, asList(estados));
    }

    public static ClienteService criarService() {
        return criarClienteService();
    }
}
