package com.github.kenyamoura.compreaqui.dominio;

public class Credenciais {
    private String email;
    private String senha;

    public Credenciais(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
