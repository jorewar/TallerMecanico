package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.FuenteDatosMemoria;

public enum FabricaFuenteDatos {
    MEMORIA{
        @Override
        public IFuenteDatos crear() {
            return new FuenteDatosMemoria();
        }
    }, MARIADB, FICHEROS;

    public abstract IFuenteDatos crear();
}
