package com.github.kenyamoura.compreaqui.view.autenticar;

import com.github.kenyamoura.compreaqui.dominio.Cliente;

/*
 * Callback utilizado pela ViewModel para se comunicar indiretamente com a Activity (View).
 */
public interface AutenticarViewCallback {

    /**
     * Evento de callback utilizado pela ViewModel ao autenticar com sucesso.
     *
     * @param cliente O cliente que foi autenticado.
     */
    void onUsuarioAutenticado(Cliente cliente);

    /**
     * Caso ocorra qualquer erro, esse método será chamado pela ViewModel.
     *
     * @param erro O erro que ocorreu
     */
    void onErroAoAutenticar(Throwable erro);
}
