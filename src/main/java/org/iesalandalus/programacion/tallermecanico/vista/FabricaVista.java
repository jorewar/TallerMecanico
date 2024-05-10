package org.iesalandalus.programacion.tallermecanico.vista;

import org.iesalandalus.programacion.tallermecanico.vista.texto.VistaTexto;

public enum FabricaVista {

    TEXTO {
        @Override
        public Vista crear() {
            return new VistaTexto();
        }
    }, GRAFICAS;

    public abstract Vista crear();
}
