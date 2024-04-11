package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente {
    private static final String ER_NOMBRE = "^[A-ZÁÉÍÓÚ][a-záéíóú]+(?: [A-ZÁÉÍÓÚ][a-záéíóú]+){0,5}$";
    private static final String ER_DNI = "^\\d{8}[A-Z]$";
    private static final String ER_TELEFONO = "^\\d{9}$";
    private String nombre;
    private String dni;
    private String telefono;

    public Cliente(String nombre, String dni, String telefono) {
        setNombre(nombre);
        setDni(dni);
        setTelefono(telefono);
    }

    public Cliente(Cliente cliente) {
        Objects.requireNonNull(cliente, "No es posible copiar un cliente nulo.");
        this.nombre = cliente.getNombre();
        this.dni = cliente.getDni();
        this.telefono = cliente.getTelefono();
    }

    public String getDni() {
        return dni;
    }

    private void setDni(String dni) {
        Objects.requireNonNull(dni, "El DNI no puede ser nulo.");
        Pattern patron = Pattern.compile(ER_DNI);
        Matcher comparador = patron.matcher(dni);
        if (!comparador.matches()) {
            throw new IllegalArgumentException("El DNI no tiene un formato válido.");
        }
        if (!comprobarLetraDni(dni)) {
            throw new IllegalArgumentException("La letra del DNI no es correcta.");
        }
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        Pattern patron = Pattern.compile(ER_NOMBRE);
        Matcher comparador = patron.matcher(nombre);
        if (!comparador.matches()) {
            throw new IllegalArgumentException("El nombre no tiene un formato válido.");
        }
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        Objects.requireNonNull(telefono, "El teléfono no puede ser nulo.");
        Pattern patron = Pattern.compile(ER_TELEFONO);
        Matcher comparador = patron.matcher(telefono);
        if (!comparador.matches()) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido.");
        }
        this.telefono = telefono;
    }

    private boolean comprobarLetraDni(String dni) {
        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        int indiceLetraDNI = numeroDNI % 23;
        char[] letrasCorrespondientesDNI = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        char letraDniPasado = dni.charAt(8);
        return letraDniPasado == letrasCorrespondientesDNI[indiceLetraDNI];
    }

    public static Cliente get(String dni) {
        return new Cliente("Cliente Predeterminado", dni, "000000000");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(dni, cliente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", this.nombre, this.dni, this.telefono);
    }
}