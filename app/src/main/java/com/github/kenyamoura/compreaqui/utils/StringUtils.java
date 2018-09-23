package com.github.kenyamoura.compreaqui.utils;

public class StringUtils {
    public static boolean isBlank(String campo) {
        return campo == null || campo.trim().isEmpty();
    }

    public static boolean isNotBlank(String campo) {
        return !isBlank(campo);
    }
}
