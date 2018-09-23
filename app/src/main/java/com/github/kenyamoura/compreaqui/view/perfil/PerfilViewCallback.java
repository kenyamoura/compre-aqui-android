package com.github.kenyamoura.compreaqui.view.perfil;

public interface PerfilViewCallback {
    void onErro(Throwable erro);
    void onPerfilCarregado();
    void onPerfilSalvoComSucesso();
    void onPerfilDestruido();
}
