![Logo OlympULL](src/main/resources/images/logo_olympull_v2.png)

# OlympULL: Aplicación de gestión de torneos

## Introducción

### ¿Qué es OlympULL?
Olympull es una aplicación de escritorio escrita en Java que ha sido desarrollada como Trabajo de Fin de Grado en el Grado de Ingeniería Informática de la Universidad de La Laguna.

Este programa sirve para la gestión de torneos por puntos, en los que los participantes van obteniendo puntos según su desempeño en diferentes ejercicios sin enfrentamientos directos entre ellos.

En concreto, este programa ha nacido ante la necesidad de un software de gestión para la Olimpiada de Pensamiento Computacional, un evento celebrado cada año en la Escuela Superior de Ingeniería y Tecnología de la Universidad de La Laguna, que tiene como objetivo incentivar las vocaciones científicas entre niños y niñas de centros de Primaria y Secundaria de la isla de Tenerife.

### ¿Qué puedo hacer con OlympULL?
Los torneos de la Olimpiada de Pensamiento Computacional están compuestos por itinerarios, que son los diferentes conjuntos o circuitos de ejercicios a los que se van a enfrentar los equipos de participantes.
Con OlympULL, la gestión de estos elementos, las relaciones entre ellos y el recuento de puntos serán tareas mucho más sencillas.
Se consideran tres tipos de usuarios:
* **Administradores.** Son los usuarios que más funcionalidades pueden realizar dentro de la aplicación. Estos pueden realizar dos tipos de gestiones:
  - **Gestión de olimpiadas.** Se trata de la creación y gestión de las olimpiadas, los itinerarios, los ejercicios y sus rúbricas, los equipos y las relaciones entre ellos.
  - **Gestión de usuarios.** Se refiere a la creación y gestión de los usuarios de la aplicación, así como las relaciones entre ellos y los diferentes elementos de las olimpiadas.

* **Organizadores.** Son las personas encargadas de cada itinerario. Pueden añadir ejercicios a los itinerarios que tengan asignados.
 
* **Monitores.** Son las personas encargadas de cada ejercicio. Pueden puntuar a cada equipo participante en los ejercicios que tengan asignados.

Además, independientemente del tipo de usuario que seas, incluso si no estás dado de alta en la aplicación, puedes consultar el ranking de equipos para cada ejercicio.

### ¿Qué necesito para utilizar OlympULL?
OlympULL requiere de las siguientes tecnologías:
#### En mi máquina local
* Java 17.0.8 o superior.

#### En mi máquina remota
* MySQL 8.0.36 o superior.

## Guía de inicio rápido
A continuación, se describen los pasos a seguir para poder utilizar este software.

### **Instalación**
* Instala la versión de Java requerida en su máquina local (si no la tienes).
* Instala la versión de MySQL requerida en su máquina remota (si no la tienes).
* Descarga este repositorio en su máquina local.

### **Configuración**
* El archivo [config.properties](src/main/resources/config.properties) contiene la información necesaria para poder establecer la conexión entre la máquina local y la base de datos de la máquina remota. Define los valores precisos para poder realizar esta conexión entre tus máquinas.
* El archivo [create-database.sql](src/main/resources/create-database.sql) contiene las sentencias SQL necesarias para crear las tablas correspondientes en la base de datos de tu máquina remota. [ScriptRunner.java](/src/java/ScriptRunner.java) te permitirá ejecutar este script de forma automática para montar la base de datos fácilmente. 

### **Uso básico**
Una vez descargado y configurado, puedes ejecutar el programa tanto desde tu IDE favorito como mediante un archivo ejecutable JAR.
Para crear el JAR, simplemente utiliza el comando <code>mvn package</code> desde el directorio raíz del proyecto. Cuando termine el proceso, el ejecutable aparecerá en <code>/target</code>.

¡Ya puedes comenzar a usar OlympULL!

La primera vez que inicies sesión en la aplicación, deberás introducir las siguientes credenciales:
- Usuario: ADMIN1
- Contraseña: ADMIN1

Posteriormente, podrás cambiar estas credenciales, o incluso crear otros usuarios de tipo administrador para gestionar la Olimpiada.

### **Recursos Adicionales**
* Se recomienda instalar la fuente de texto Argentum, que es la que se ha establecido para los elementos de la interfaz gráfica. Puedes encontrarla en el directorio [/fonts](/fonts) de este repositorio.
* El archivo [data.properties](src/main/resources/data.properties) contiene la ruta y los nombres de los archivos de datos de *back-up*. Ya se han establecido unos nombres por defecto, pero puedes cambiarlos por otros que te gusten más, si así lo prefieres.

### **Distribución a organizadores y monitores**
La distribución de la aplicación a los usuarios organizadores y monitores se hará mediante un archivo JAR. Una vez hayan sido dados de alta en la aplicación, podrán iniciar sesión y comenzar a utilizar OlympULL.
Las credenciales establecidas en las propiedades <code>ssh_user</code>, <code>ssh_password</code>, <code>db_user</code> y <code>db_password</code> en [config.properties](src/main/resources/config.properties) representan el usuario y la contraseña necesarios para acceder a la máquina remota y a la base de datos de la misma, respectivamente. Para asegurar que no se hagan modificaciones maliciosas o indeseadas sobre cualquiera de ellas, mientras que los administradores deberían poder tener el control absoluto de ambas, se recomienda encarecidamente la creación de usuarios con permisos limitados y la modificación de [config.properties](src/main/resources/config.properties) para incorporar las credenciales de estos nuevos usuarios antes de distribuir un ejecutable de OlympULL a los organizaodres y monitores, puesto que toda la información de inicio de sesión puede ser fácilmente accesible si se inspeccionan los contenidos del JAR.

## Licencia

Este proyecto está licenciado bajo la [Licencia Creative Commons Atribución-NoComercial-SinDerivadas 4.0 Internacional](LICENSE.md).
