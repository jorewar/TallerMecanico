import org.iesalandalus.programacion.tallermecanico.vista.graficas.VistaVentanas;
import org.iesalandalus.programacion.tallermecanico.vista.graficas.utilidades.Controladores;
import org.iesalandalus.programacion.tallermecanico.vista.graficas.utilidades.Dialogos;

public class LanzadorVentanaPrincipal extends Application{

        public void start(Stage stage) throws Exception {
            Controlador ventanaPrincipal = Controladores.get (vistaFxml: "/vistas/VentanaPrincipal.fxml", "Taller Mecanico", null);
            ventanaPrincipal.addHojasEstilos("/estilos/estilos.css");
            Dialogos.setHojaEstilos("estilos/estilos.css");
            ventanaPrincipal.addIcono("/imagenes/iconoTaller.png");
            ventanaPrincipal.getEscenario().setOnCloseRequest(e -> salir(e, ventanaPrincipal.getEscenario()));
            VistaVentanas.getInstancia().setVentanaPrincipal(ventanaPrincipal);
            VistaVentanas.getInstancia().getGestorEventos().notificaciones



                    }



}


public static void comenzar () {launch (LanzadorVentanaPrincipal.class);}
