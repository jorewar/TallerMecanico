package org.iesalandalus.programacion.tallermecanico.vista.graficas;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import java.util.Objects;



public class VistaVentanas implements Vista {
    public float leerPrecioMaterial() {
        }
        return precioMaterial;
    }

    public LocalDate leerFechaCierre() {
        leerFechaFin leerFechaFin = (LeerFechaFin) Controladores.get("/vistas/LeerFechaFin.fxml", "Leer fecha fin", ventanaPrincipal.getEscenario());
        leerFechaFin.getEscenario().showAnWait();
        return Objects.requireNonNull(leerFechaFin.getFechaFin(), "Operacion cancelada por el usuario");
    }

    public void notificarResultado(Evento evento, String texto, boolean exito) {
        system.out.println(evento + textp + exito);
        if (exito) {
            Dialogos.mostarDialogoInformacion(evento.toString(), texto, ventanaPrincipal.getEscenario());
        } else {
            Dialogos.mostarDialogoInformacion(evento.toString(), texto, ventanaPrincipal.getEscenario());
        }
    }
    private List<Vehiculo> vehiculos;

    public static VistaVentanas getInstance() {
        if (instancia == null) {
            instancia = new VistaVentanas();
        }
        return instancia;
    }

    private VistaVentanas(){}

    public List <Vehiculo> getVehiculos() {return vehiculos;}

    public GestorEventos getGestorEventos(){ return gestorEventos; }

    void setVentanaPrincipal (Controlador)


}
