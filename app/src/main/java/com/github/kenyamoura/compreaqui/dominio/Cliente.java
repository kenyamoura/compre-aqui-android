package com.github.kenyamoura.compreaqui.dominio;

public class Cliente {
    private String email;
    private String senha;
    private String cpf;
    private String nome;
    private String endereco;
    private String estado;
    private String municipio;
    private String telefone;

    public Cliente(String email,
                   String senha,
                   String cpf,
                   String nome,
                   String endereco,
                   String estado,
                   String municipio,
                   String telefone) {
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
        this.estado = estado;
        this.municipio = municipio;
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEstado() {
        return estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getTelefone() {
        return telefone;
    }
}
