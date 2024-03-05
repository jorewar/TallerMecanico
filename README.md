# Tarea: Taller mecánico
## Profesor: José Ramón Jiménez Reyes
## Alumno: jorge cortes cortes

Al cliente le ha gustado bastante la aplicación, pero nos comenta algunas mejoras que necesita la anterior versión y nuevas funcionalidades que le gustaría que tuviese. Todo ello lo abordaremos en este **segundo sprint**.

Nos comenta que el taller realiza dos tipos de trabajos:
- **Revisiones**: Son las revisiones rutinarias que se realizan antes de pasar la ITV o revisiones de seguridad. A estos trabajos solo se le pueden añadir horas, dependiendo de lo que los mecánicos tarden en realizarlos. Si en la revisión se detecta que hay que cambiar algo, habría que cerrar la revisión y abrir un nuevo trabajo mecánico.
- **Mecánicos**: Son los trabajos en los que se sustituyen piezas defectuosas, se corrigen errores de funcionamiento, etc. A este tipo de trabajos los mecánicos pueden, además de añadirles horas, añadirles precio del material que reemplazan.

A la hora de facturar los trabajos, siempre se cobra un **precio fijo**, que se calcula como la multiplicación del **número de días** que el vehículo ha permanecido en el taller por **10**.

A este precio fijo se le añade el **precio específico** de cada trabajo que se calcula de la siguiente forma:
- **Revisiones**: Sería el **número de horas** dedicadas por los mecánicos por **35**.
- **Mecánicos**: Se calcula como el **número de horas** por **30**, más el **precio del material** reemplazado por **1.5**.

También nos comenta que, tanto para **añadir horas a un trabajo**, **añadir precio de material a un trabajo mecánico** o **cerrar un trabajo**, no es operativo la forma en que se venía realizando. Le gustaría que para estas operaciones simplemente pidiese la matrícula del vehículo y el dato en cuestión, ya que un vehículo solo puede tener un trabajo abierto y sería mucho más cómodo para los mecánicos.

Por tanto, todo esto lo abordaremos en este **segundo sprint**. Además, vamos a aprovechar este **sprint** para **refactorizar** lo que llevamos implementado hasta ahora para implementar de una forma adecuada el patrón **Modelo-Vista-Controlador**, ya que prevemos que tendremos varias vistas y varias modelos para nuestra aplicación. Supondremos que en el futuro podremos tener **varios modelos** y que cada uno de ellos puede trabajar con diferentes **fuentes de datos**. También supondremos que tendremos **varias vistas**. Para ello implementaremos los siguientes **patrones de diseño**:
- **Observador (Observer)**: Utilizado para evitar el mayor problema de diseño en el sprint anterior: la dependencia cíclica que existe entre la vista y el controlador.
- **Método de fábrica (Factory Method)**: Utilizado para crear las vistas y los modelos. 
- **Fábrica abstracta (Abstract Factory)**: Para crear las fuentes de datos.
- **Método plantilla (Template Method)**: Para el cálculo del precio. 

Todo esto, aparte de algunos **patrones utilizados en el sprint anterior** como eran:
- **Métodos estáticos de fábrica**: Utilizados en los métodos `get` de las clases de dominio.
- **Objeto de transferencia de datos (Data Transfer Object - DTO)**: Utilizado en las clases de dominio.
- **Objeto de acceso a los datos (Data Access Object - DAO)**: Utilizado en las clases de negocio.

En este repositorio hay un esqueleto de proyecto **gradle** con las dependencias necesarias del proyecto y que, en la rama `refactorizacion_herencia`, ya lleva incluidos todos los test necesarios que el modelo debe pasar, con todo lo que hemos comentado.

Para ello te muestro un diagrama de clases (en el que cuando se expresa cardinalidad `*` queremos expresar que se hará uso de **listas**) para el mismo y poco a poco te iré explicando los diferentes pasos a realizar:

![Diagrama de clases de la tarea](src/main/resources/uml/tallerMecanico.png)


#### Primeros Pasos
1. Lo primero que debes hacer es mezclar tu rama `sprint_inicial` con la rama `master`.
2. Luego crea un nueva rama etiquetada como `refactorizacion_herencia`, a partir de la rama `master` que anteriormente habías mezclado..
3. Añade el remote de mi repositorio como `profesor` y haz un `pull`.
4. Cambiate a tu rama `master` y mezclala con la master de `profesor`. Añade tu nombre al `README.md`, haz `commit` y `push`a tu repositorio en la rama `master`.
5. Cambia tu repositorio a la rama `refactorizacion_herencia` y mezclala con la misma rama de `profesor`.
6. Modifica el archivo `README.md` para que incluya tu nombre en el apartado "Alumno". Realiza tu **primer commit** en esta nueva rama.

#### Adecuación del `dominio`
1. Extrae la clase abstracta `Trabajo` a partir de la clase `Revision` anterior.
2. Adecúa la clase `Trabajo` para que quede tal y como se muestra en el diagrama de clases. El método `copiar`copiará un trabajo llamando al constructor copia de la clase específica y para ello deberá saber si el parámetro que se pasa es instancia de una u otra clase. El método `get` devolverá un trabajo, una revisión por ejemplo, con un cliente cualquiera, una fecha de inicio cualquiera y el vehículo indicado.
3. Adecúa la clase `Revision` para que quede como se muestra en el diagrama y se comporte como se indica en el enunciado.
4. Crea la clase `Mecanico` para que quede como se muestra en el diagrama y se comporte como se indica en el enunciado.
5. Comprueba que las clases de este paquete **pasan los tests** y cuando lo hagan haz un **commit**.

#### Adecuación del paquete `negocio`
1. Crea el nuevo paquete `memoria`.
2. Modifica la clase `Trabajos` para que los métodos `anadirHoras`, `anadirPrecioMaterial` y `cerrar` se basen en el trabajo abierto para el vehículo indicado en el trabajo que se pasa como parámetro.
3. Extrae las interfaces de las clases.
4. Crea la clase `FuenteDatosMemoria` y extra su interfaz.
5. Crea el enumerado `FabricaFuenteDatos` que implementará la fábrica de modelos que por ahora solo tendrá la instancia `MEMORIA`.
6. Comprueba que las clases de este paquete **pasan los tests** y cuando lo hagan haz un **commit**.

#### Adecuación del paquete `modelo`
1. Extrae la interfaz y renombra la clase para el modelo.
2. Crea la fábrica de modelos, que por ahora solo tendrá la instancia `CASCADA`.
3. Comprueba que las clases de este paquete **pasan los tests** y cuando lo hagan haz un **commit**.

#### Adecuación del paquete `vista`
1. Crea el paquete `eventos` y pasa el enumerado `Opcion` a este paquete. Renombra dicho enumerado a `Evento` y haz que los métodos se adecuen a lo expresado en el diagrama de clases.
2. Crea la interfaz `ReceptorEventos` tal y como se muestra en el diagrama.
3. Crea la clase `GestorEventos` que contendrá un mapa asociando a cada evento la lista de subscriptores (objetos que implementen la interfaz `ReceptorEventos`).
4. Crea el paquete `texto` y adecua la clase `Consola` al diagrama (mueve los métodos necesarios a `Vista` y cambia la visibilidad de los restantes).
5. Modifica la clase `Vista` a lo expresado en el diagrama.
6. Renombra la clase `Vista` y extrae su interfaz.
7. Crea el enumerado `FabricaVista` que implementará la fábrica para las vistas y que por ahora solo tendrá la instancia `TEXTO`.
8. Realiza un **commit**.

#### Adecuación del paquete `controlador`
1. Adecúa la clase `Controlador` a lo expresado en el diagrama. Deberá suscribirse al gestor de eventos de la vista para todos los eventos y en el método `actualizar` los tratará interactuando con la vista y con el modelo para llevar a cabo las diferentes operaciones.
2. Extrae la interfaz `IControlador` de la clase anterior que hereda de la interfaz `ReceptorEventos`.
3. Realiza un **commit**.

#### Adecuación de la clase `Main`
1. Haz que esta clase utilice las diferentes fábricas para que todo funcione correctamente.
2. Comprueba que todo funciona correctamente, realiza un **commit** y seguidamente realiza el **push** a tu repositorio remoto.


#### Se valorará:

- La indentación debe ser correcta en cada uno de los apartados.
- Los identificadores utilizados deben ser adecuados y descriptivos.
- Se debe utilizar la clase `Entrada` para realizar la entrada por teclado que se encuentra como dependencia de nuestro proyecto en la librería entrada.
- El programa debe pasar todas las pruebas que van en el esqueleto del proyecto y toda entrada del programa será validada, para evitar que el programa termine abruptamente debido a una excepción.
- La corrección ortográfica tanto en los comentarios como en los mensajes que se muestren al usuario.


