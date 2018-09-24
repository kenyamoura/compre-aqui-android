package com.github.kenyamoura.compreaqui.view.cadastro;

import com.github.kenyamoura.compreaqui.dominio.Cliente;

/*
 * Callback utilizado pela ViewModel para se comunicar indiretamente com a Activity (View).
 */
public interface CadastroViewCallback {

    /**
     * Evento de callback chamado pela ViewModel caso o cliente seja cadastrado com sucesso.
     *
     * @param cliente O cliente recém cadastrado
     */
    void onClienteCadastradoComSucesso(Cliente cliente);

    /**
     * Caso ocorra qualquer erro, esse método será chamado pela ViewModel.
     *
     * @param erro O erro que ocorreu
     */
    void onErroAoCadastrar(Throwable erro);
}
