package org.iesalandalus.programacion.tallermecanico.controlador;

import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.ReceptorEventos;

public interface IControlador extends ReceptorEventos {
    void comenzar();

    void terminar();

    @Override
    void actualizar(Evento evento);
}
