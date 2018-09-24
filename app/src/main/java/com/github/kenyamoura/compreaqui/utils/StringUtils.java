package com.github.kenyamoura.compreaqui.utils;

public class StringUtils {

    /**
     * Método utilizado utilizado para checar se um texto é nulo ou vazio.
     *
     * @param campo um texto qualquer.
     * @return true se for nulo ou vazio (somente espaços é considerado vazio).
     */
    public static boolean isBlank(String campo) {
        return campo == null || campo.trim().isEmpty();
    }

}
