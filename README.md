🚀 Spring Docker Demo – Despliegue en AWS EC2
Este proyecto empaqueta una aplicación Spring Boot en un contenedor Docker y la despliega en AWS EC2 utilizando Docker Hub como repositorio de imágenes.
---
```text
springDockerDemo/
│   docker-compose.yml
│   Dockerfile
│   pom.xml
│
├───src
│   ├───main
│   │   └───java
│   │       └───co
│   │           └───edu
│   │               └───escuelaing
│   │                   ├── HelloRestController.java
│   │                   └── RestServiceApplication.java
│   └───test
│       └───java
│           └───co
│               └───edu
│                   └───escuelaing
│                       └── AppTest.java
└───target
    └── springDockerDemo-1.0-SNAPSHOT.jar


```
---
🧩 Clases Principales
1. RestServiceApplication.java
```bash
@SpringBootApplication
public class RestServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RestServiceApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", getPort()));
        app.run(args);
    }
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; // Puerto por defecto
    }
}

```

2. HelloRestController.java

```bash
@RestController
public class HelloRestController {
    private static final String template = "Hello, %s!";
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }
}


```
---

⚙️ Requerimientos Mínimos

Java 21 (usamos eclipse-temurin:21-jre como base de Docker)

Maven 3.8+ para construir el proyecto.

Docker 20.x o superior.

Cuenta en Docker Hub para subir la imagen.

Cuenta AWS Academy para EC2.
---
🌐 Puertos Utilizados
Descripción	Puerto
Puerto interno en el contenedor Docker	6000
Puerto por defecto de la app	5000 (solo local si no se define PORT)
Puerto público en AWS EC2	42000 (expuesto en Security Group)
Puerto MongoDB (docker-compose local)	27017
Puerto host local (compose web)	8087 (map a 6000)
---
📝 Archivos Clave
Dockerfile
```bash
FROM eclipse-temurin:21-jre
WORKDIR /usrapp/bin
ENV PORT 6000
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency
CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.RestServiceApplication"]

```
---
docker-compose.yml

Permite levantar web (Spring) + MongoDB localmente:

```bash
version: '2'
services:
  web:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: web
    ports:
      - "8087:6000"
  db:
    image: mongo:3.6.1
    container_name: db
    volumes:
      - mongodb:/data/db
      - mongodb_config:/data/configdb
    ports:
      - "27017:27017"
    command: mongod
volumes:
  mongodb:
  mongodb_config:

```
---
🔨 Construcción y Publicación de la Imagen

Compilar el proyecto y copiar dependencias:

```bash
mvn clean package
mvn dependency:copy-dependencies
```

Construir la imagen:
```bash
docker build -t dockerspringdemo .
```

Login en Docker Hub:
```bash
docker login
```

Tag y Push:
```bash
docker tag dockerspringdemo santiagoarteaga/springdocker-demo:latest
docker push santiagoarteaga/springdocker-demo:latest
```
---

<img width="518" height="159" alt="image" src="https://github.com/user-attachments/assets/ed9ac3e4-0a7d-4342-8125-5c2d76ef6cdd" />
<img width="602" height="150" alt="image" src="https://github.com/user-attachments/assets/149e16b0-1e05-4750-9e23-6937cb7256ab" />
<img width="649" height="297" alt="image" src="https://github.com/user-attachments/assets/134af9ec-249e-4602-bd1e-612e9021f35e" />
<img width="604" height="290" alt="image" src="https://github.com/user-attachments/assets/dd68d476-1b7a-499f-be18-448a974913a1" />



Evidencias:
