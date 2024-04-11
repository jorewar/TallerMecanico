package org.iesalandalus.programacion.tallermecanico.vista.eventos;

import java.util.*;

public class GestorEventos {
    Map<Evento, List<ReceptorEventos>> receptores = new EnumMap<>(Evento.class);

    public GestorEventos(Evento... eventos) {
        Objects.requireNonNull(eventos, "Se debe gestionar algún evento.");
        for (Evento evento : eventos) {
            receptores.put(evento, new ArrayList<>());
        }
    }

    public void suscribir(ReceptorEventos receptor, Evento... eventos) {
        Objects.requireNonNull(receptor, "El receptor de eventos no puede ser nulo.");
        Objects.requireNonNull(eventos, "Te debes suscribir a algún evento.");
        for (Evento evento : eventos) {
            List<ReceptorEventos> suscriptores = receptores.get(evento);
            suscriptores.add(receptor);
        }
    }

    public void desuscribir(ReceptorEventos receptor, Evento... eventos) {
        Objects.requireNonNull(receptor, "El receptor de eventos no puede ser nulo.");
        Objects.requireNonNull(eventos, "Te debes suscribir a algún evento.");
        for (Evento evento : eventos) {
            List<ReceptorEventos> suscriptores = receptores.get(evento);
            suscriptores.remove(receptor);
        }
    }

    public void notificar(Evento evento) {
        Objects.requireNonNull(evento, "No se puede notificar un evento nulo.");
        for (ReceptorEventos receptorEventos : receptores.get(evento)) {
            receptorEventos.actualizar(evento);
        }
    }
}