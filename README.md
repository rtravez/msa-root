# Crear la base de datos en PostgresSQL
`CREATE DATABASE db_test`

Verificar que la base de datos esté corriendo por el puerto por defecto `5432`.
El usuario por defecto de la base es `postgres`
La contraseña es: `admin`

Importar el script **`BaseDatos.sql`**

Sí prefiere cambiar las configuraciones en el archivo `application.properties `

## Development server
Ejecutar `java -jar msa-service-0.0.1-SNAPSHOT.jar` por defecto `http://localhost:1987/msaServices`.

Los datos de inicio de sesión para oauth: `http://localhost:8080/mscServices/oauth/token`

Usuario: **admin**
Contraseña: **12345**
#
Pruebas Postman
`Sofka.postman_collection.json`
#

#Imagenes docker:
#
https://hub.docker.com/r/rtravez/service-postgres-server
#
https://hub.docker.com/r/rtravez/msc-service
#
https://hub.docker.com/r/rtravez/msa-service

## Uso de `docker-compose.yml`

El archivo `docker-compose.yml` permite levantar los servicios del proyecto con un solo comando. Antes de ejecutarlo, verifica que tengas instalado Docker y Docker Compose.

### Servicios incluidos

- `service-postgres-server`: base de datos PostgreSQL.
- `msc-service`: servicio principal de la aplicación.
- `msa-service`: servicio adicional que depende de `msc-service`.

### Configuración usada por defecto

- Base de datos: `db_test`
- Usuario de PostgreSQL: `postgres`
- Contraseña de PostgreSQL: `admin`
- Puerto PostgreSQL: `5432`
- Puerto `msc-service`: `8080`
- Puerto `msa-service`: `1987`

### Levantar los servicios

Desde la raíz del proyecto ejecuta:

```bash
docker compose up -d
```

Si tu entorno usa la sintaxis antigua, también puedes usar:

```bash
docker-compose up -d
```

Con esto se descargan las imágenes definidas en el archivo y se crean los contenedores en segundo plano.

### Verificar el estado

```bash
docker compose ps
```

### Ver logs

```bash
docker compose logs -f
```

### Detener los servicios

```bash
docker compose down
```

Si además quieres eliminar los volúmenes y borrar los datos persistidos:

```bash
docker compose down -v
```

### Persistencia de datos

El archivo usa volúmenes Docker para conservar los datos de PostgreSQL y los archivos temporales de Spring entre reinicios:

- `pgdata` para la base de datos.
- `springdata` para los servicios Java.

### Acceso a los servicios

Una vez levantado el entorno:

- PostgreSQL: `localhost:5432`
- OAuth token: `http://localhost:8080/mscServices/oauth/token`
- `msc-service`: `http://localhost:8080/mscServices`
- `msa-service`: `http://localhost:1987/msaServices`

Si cambias los puertos o credenciales, ajusta el archivo `docker-compose.yml` y, si aplica, la configuración de `application.properties`.

# msa-root

Prueba Técnica -> Arquitectura Microservicio

El proyecto base de Spring Boot tiene una estructura en capas, basado en subproyectos, dicha estructura facilita la
gestión y la comprensión del código.

A continuación se muestra la descripción general de la estructura en capas para el proyecto:

## Capa de Controladores (Controllers):

Esta capa contiene clases que actúan como controladores REST o controladores MVC. Se encargan de recibir las solicitudes
HTTP, procesarlas y devolver las respuestas adecuadas.
Los controladores suelen tener anotaciones como @RestController o @Controller para definir puntos de acceso HTTP y
métodos de manipulación de datos.

## Capa de servicios (Services):

La capa de servicios contiene la lógica de negocio de la aplicación.
Aquí se implementan los métodos que realizan operaciones específicas relacionadas con la lógica de negocio, como la
manipulación de datos, la realización de cálculos, la integración con otros sistemas, etc.
Los servicios suelen ser interfaces con implementaciones concretas y se anotan con @Service.

## Capa de Repositorios (Repositories):

Esta capa se encarga de interactuar con la base de datos o cualquier otro mecanismo de almacenamiento de datos.
Aquí se definen las interfaces de repositorio que especifican métodos para realizar operaciones de lectura y escritura
en la base de datos, como guardar, actualizar, eliminar y consultar entidades.
Los repositorios suelen ser interfaces que extienden JpaRepository u otras interfaces proporcionadas por Spring Data y
se anotan con @Repository.

## Capa de Modelos (Models):

Esta capa contiene las clases que representan los datos de la aplicación, como entidades JPA, DTO (Data Transfer
Objects), o cualquier otra clase utilizada para transferir datos entre las capas.
Los modelos pueden incluir anotaciones como @Entity, @Data, @Getter, @Setter, etc., según sea necesario.

## Capa client (msa-client)

Esta capa es un subproyecto y se encarga de alojar:
* Entidades
* Interfaces de servicios
* Interfaces de repositorios
* Excepciones personalizadas
* Utilitarios comunes
* Constantes

## Capa core (msa-core)

La capa core es prácticamente el corazón de todo el proyecto, aquí encontraremos las reglas de negocio y la lógica de
programación.
Aquí se alojan los siguientes apartados:

* Configuraciones
* Implementación de repositorios
* Implementación de servicios

## Capa dto. (msa-dto)

En este subproyecto básicamente encontramos lo que son las clases que representan objetos utilizados para transmitir
datos entre capas.

## Capa service (msa-service)

La capa services es un subproyecto que corresponde a los controladores, se encarga de exponer las API o las
funcionalidades del proyecto.
Básicamente, corresponde a la capa de controllers y es la primera capa donde inicia todo.
Un "endpoint" en la capa de controladores (controllers) se refiere a un punto de acceso específico dentro de una
aplicación que permite la comunicación entre el cliente y el servidor.
Los endpoints son gestionados por los controladores y están asociados a acciones u operaciones específicas que pueden
ser realizadas por la aplicación.
