# OlympULL: Aplicación de gestión de torneos

## Introducción

### ¿Qué es OlympULL?
Olympull es una aplicación de escritorio escrita en Java que ha sido desarrollada como Trabajo de Fin de Grado en el Grado de Ingeniería Informática de la Universidad de La Laguna.

Este programa sirve para la gestión de torneos por puntos, en los que los participantes van obteniendo puntos según su desempeño en diferentes ejercicios sin enfrentamientos directos entre ellos.

En concreto, el principal propósito de este programa es la gestión de la Olimpiada de Pensamiento Computacional, un evento celebrado cada año en la Escuela Superior de Ingeniería y Tecnología de la Universidad de La Laguna, que tiene como objetivo incentivar las vocaciones científicas entre niños y niñas de centros de Primaria y Secundaria de la isla de Tenerife.

### ¿Qué necesito para utilizar OlympULL?
OlympULL requiere de las siguientes tecnologías:
#### En mi máquina local
* Java 17.0.8 o superior.


#### En mi máquina remota
* MySQL 8.0.36 o superior.

## Guía de inicio rápido
A continuación, se describen los pasos a seguir para poder utilizar este software.

1. **Instalación**:
* Descarga este repositorio en su máquina local.
* Instala la versión de Java requerida en su máquina local (si no la tienes).
* Instala la versión de MySQL requerida en su máquina remota (si no la tienes).

2. **Configuración**:
* El archivo [config.properties](config.properties) contiene la información necesaria para poder establecer la conexión entre la máquina local y la base de datos de la máquina remota. Define los valores precisos para poder realizar esta conexión entre tus máquinas.
* El archivo [create-database.sql](create-database.sql) contiene las sentencias SQL necesarias para crear las tablas correspondientes en la base de datos de tu máquina remota. [ScriptRunner.java](/src/java/ScriptRunner.java) te permitirá ejecutar este script de forma automática. 


3. **Uso Básico**:
   - [Ejemplos de comandos o acciones para utilizar el software]

4. **Recursos Adicionales**:
* Se recomienda instalar la fuente de texto Argentum, que es la que se ha establecido para los elementos de la interfaz gráfica. Puedes encontrarla en el directorio [/fonts](/fonts) de este repositorio.
* El archivo [data.properties](data.properties) contiene la ruta y los nombres de los archivos de datos de *back-up*. Ya se han establecido unos nombres por defecto, pero puedes cambiarlos por otros que te gusten más, si así lo prefieres.

## Licencia

Este proyecto está licenciado bajo la [Licencia XYZ](LICENSE.md).
