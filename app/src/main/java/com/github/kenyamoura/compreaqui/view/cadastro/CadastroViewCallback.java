package com.github.kenyamoura.compreaqui.view.cadastro;

import com.github.kenyamoura.compreaqui.dominio.Cliente;

public interface CadastroViewCallback {
    void onClienteCadastradoComSucesso(Cliente cliente);
    void onErroAoCadastrar(Throwable erro);
}
