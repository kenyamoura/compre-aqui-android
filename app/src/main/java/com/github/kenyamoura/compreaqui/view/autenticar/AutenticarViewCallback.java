package com.github.kenyamoura.compreaqui.view.autenticar;

import com.github.kenyamoura.compreaqui.dominio.Cliente;

public interface AutenticarViewCallback {
    void onUsuarioAutenticado(Cliente cliente);

    void onErroAoAutenticar();
}
