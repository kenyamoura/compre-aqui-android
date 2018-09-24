package com.github.kenyamoura.compreaqui.view.perfil;

/*
 * Callback utilizado pela ViewModel para se comunicar indiretamente com a Activity (View).
 */
public interface PerfilViewCallback {
    /**
     * Caso ocorra qualquer erro, esse método será chamado pela ViewModel.
     *
     * @param erro Erro que ocorreu.
     */
    void onErro(Throwable erro);

    // Método utilizado para alertar a View que o perfil foi carregado.
    void onPerfilCarregado();

    // Método utilizado para alertar quando o perfil for salvo com sucesso.
    void onPerfilSalvoComSucesso();

    // Método utilizado para alertar quando o perfil for destruido com sucesso.
    void onPerfilDestruido();
}
