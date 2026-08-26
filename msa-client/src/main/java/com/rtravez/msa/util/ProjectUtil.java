package com.rtravez.msa.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rtravez
 */
@Slf4j
public final class ProjectUtil {

    private ProjectUtil() {
    }

    public static boolean validarNumero(String cadena) {
        return cadena.matches("^(?:\\+|-)?\\d+(\\.\\d+(E-\\d+)?)?$");
    }

    public static boolean isNotNumeric(String cadena) {
        return !cadena.matches("\\d*");
    }

    public static boolean isCero(String valor) {
        boolean mOK = false;
        if (validarNumero(valor)) {
            double valorDouble = Double.parseDouble(valor);
            if (valorDouble == 0.0D) {
                mOK = true;
            }
        }
        return mOK;
    }

    /**
     * Verifica si es una cédula válida.
     *
     * @param cedula El número de RUC (10 dademágitos)
     * @return true si la cédula es válida, caso contrario false
     */
    public static boolean isCedulaValido(final String cedula) {
        return validaIdentificacion(CEDULA, cedula);
        // return validaRucCedula(cedula) == TipoDocumentoIdentidad.CEDULA_VALIDA;
    }

    public static boolean isRucValido(final String ruc) {
        return validaIdentificacion(RUC, ruc);
    }

    /**
     * Verifica si es un RUC o cédula válido.
     *
     * @param rucCedula El número de RUC o cédula (10 - 13 digitos)
     * @return true si es cedula o ruc válido
     */
    // nuevo algoritmo RUC CEDULA PASAPORTE
    public static boolean isRucCedulaValido(final String rucCedula) {
        if (rucCedula == null) {
            return false;
        }
        switch (rucCedula.length()) {
            case 13:
                return validaIdentificacion(RUC, rucCedula);
            case 10:
                return validaIdentificacion(CEDULA, rucCedula);
            default:
                return false;
        }

    }

    public static final Character CEDULA = 'C';

    public static final Character PASAPORTE = 'P';

    public static final Character RUC = 'R';

    public static boolean validaIdentificacion(Character tipoDocumento, String identificacion) {
        boolean mOK = false;
        boolean esRuc = false;
        String cedula;
        if (tipoDocumento.equals(PASAPORTE) && identificacion.length() > 3) {
            mOK = true;
        } else if (isNotNumeric(identificacion)) {
            mOK = false;
        } else if (tipoDocumento.equals(RUC) && (identificacion.length() != 13 || !identificacion.substring(10).equals("001"))) {
            mOK = false;
        } else if (tipoDocumento.equals(CEDULA) && identificacion.length() != 10) {
            mOK = false;
        } else {
            cedula = identificacion.substring(0, 10);
            int vec3 = Integer.parseInt(identificacion.substring(2, 3));
            if ((vec3 >= 0 && vec3 <= 5) || tipoDocumento.equals(CEDULA)) {
                mOK = validaCedula(cedula);
//                if (!mOK && tipoDocumento.equals(Constantes.RUC)) {
//                    mOK = true;
//                }
            } else if (vec3 == 6) {
                mOK = valida6(cedula);
                // esRuc = true;
            } else if (vec3 == 9) {
                mOK = valida9(cedula);
                esRuc = true;
            }
            if (esRuc && !mOK) {
                mOK = true;
            }
        }
        return mOK;
    }

    private static boolean validaCedula(String cedula) {
        boolean resultado = false;
        int digito10 = Integer.parseInt(cedula.substring(9));
        int suma = 0;
        int cont = 0;
        int contPos = 0;
        while (contPos < 9) {
            contPos = 2 * cont + 1;
            int digito = Integer.parseInt(cedula.substring(contPos - 1, contPos));
            int multi = digito * 2;
            if (multi >= 10) {
                multi = 1 + multi % 10;
            }
            suma += multi;
            cont++;
        }
        cont = 1;
        contPos = 1;
        while (contPos < 8) {
            contPos = 2 * cont;
            int digito = Integer.parseInt(cedula.substring(contPos - 1, contPos));
            suma += digito;
            cont++;
        }
        int cociente = suma / 10;
        int decena = (cociente + 1) * 10;
        int verificador = decena - suma;
        if (verificador == 10) {
            verificador = 0;
        }
        if (verificador == digito10) {
            resultado = true;
        }
        return resultado;
    }

    private static boolean valida6(String cedula) {
        int digito1 = Integer.parseInt(cedula.substring(0, 1)) * 3;
        int digito2 = Integer.parseInt(cedula.substring(1, 2)) * 2;
        int digito3 = Integer.parseInt(cedula.substring(2, 3)) * 7;
        int digito4 = Integer.parseInt(cedula.substring(3, 4)) * 6;
        int digito5 = Integer.parseInt(cedula.substring(4, 5)) * 5;
        int digito6 = Integer.parseInt(cedula.substring(5, 6)) * 4;
        int digito7 = Integer.parseInt(cedula.substring(6, 7)) * 3;
        int digito8 = Integer.parseInt(cedula.substring(7, 8)) * 2;
        boolean respuesta;
        digito1 = sumaDigito(digito1);
        digito2 = sumaDigito(digito2);
        digito3 = sumaDigito(digito3);
        digito4 = sumaDigito(digito4);
        digito5 = sumaDigito(digito5);
        digito6 = sumaDigito(digito6);
        digito7 = sumaDigito(digito7);
        digito8 = sumaDigito(digito8);
        int suma = digito1 + digito2 + digito3 + digito4 + digito5 + digito6 + digito7 + digito8;
        int suma2 = Integer.parseInt(cedula.substring(0, 1)) * 3 + Integer.parseInt(cedula.substring(1, 2)) * 2
                + Integer.parseInt(cedula.substring(2, 3)) * 7 + Integer.parseInt(cedula.substring(3, 4)) * 6
                + Integer.parseInt(cedula.substring(4, 5)) * 5 + Integer.parseInt(cedula.substring(5, 6)) * 4
                + Integer.parseInt(cedula.substring(6, 7)) * 3 + Integer.parseInt(cedula.substring(7, 8)) * 2;
        int verificador = 11 - suma % 11;
        int verificador2 = 11 - suma2 % 11;
        if (verificador == 11 || verificador2 == 11) {
            verificador = 0;
        }
        if (verificador == Integer.parseInt(cedula.substring(8, 9)) || verificador2 == Integer.parseInt(cedula.substring(8, 9))) {
            respuesta = true;
        } else {
            respuesta = validaCedula(cedula);
        }
        return respuesta;
    }

    private static int sumaDigito(Integer digito) {
        int suma = digito;
        int valor1;
        int valor2;
        if (digito > 9) {
            valor1 = Integer.parseInt(digito.toString().substring(0, 1));
            valor2 = Integer.parseInt(digito.toString().substring(1, 2));
            suma = valor1 + valor2;
            if (suma > 9) {
                suma = sumaDigito(suma);
            }
        }
        return suma;
    }

    private static boolean valida9(String cedula) {
        boolean resultado = false;
        int digito1 = Integer.parseInt(cedula.substring(0, 1));
        int digito2 = Integer.parseInt(cedula.substring(1, 2));
        int digito3 = Integer.parseInt(cedula.substring(2, 3));
        int digito4 = Integer.parseInt(cedula.substring(3, 4));
        int digito5 = Integer.parseInt(cedula.substring(4, 5));
        int digito6 = Integer.parseInt(cedula.substring(5, 6));
        int digito7 = Integer.parseInt(cedula.substring(6, 7));
        int digito8 = Integer.parseInt(cedula.substring(7, 8));
        int digito9 = Integer.parseInt(cedula.substring(8, 9));
        int digito10 = Integer.parseInt(cedula.substring(9));
        int suma = digito1 * 4 + digito2 * 3 + digito3 * 2 + digito4 * 7 + digito5 * 6 + digito6 * 5 + digito7 * 4 + digito8 * 3 + digito9 * 2;
        int verificador = 11 - suma % 11;
        if (verificador == 11) {
            verificador = 0;
        }
        if (verificador == 10) {
            resultado = false;
        } else if (verificador == digito10) {
            resultado = true;
        }
        return resultado;
    }



}
