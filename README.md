Demostración en Video 📹

Puedes ver una demostración completa de esta aplicación en funcionamiento en el siguiente video:
https://youtu.be/bNUlPsZA4oM 

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

## 📸 Evidencias del proceso

<p align="center">
  <img src="https://github.com/user-attachments/assets/ed9ac3e4-0a7d-4342-8125-5c2d76ef6cdd" width="350" height="200">
  <img src="https://github.com/user-attachments/assets/149e16b0-1e05-4750-9e23-6937cb7256ab" width="350" height="200">
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/134af9ec-249e-4602-bd1e-612e9021f35e" width="350" height="200">
  <img src="https://github.com/user-attachments/assets/dd68d476-1b7a-499f-be18-448a974913a1" width="350" height="200">
</p>

### 🐳 Tercera parte: Subir la imagen a Docker Hub

1. **Crea una cuenta en [Docker Hub](https://hub.docker.com/)**.  
2. **Verifica tu correo electrónico** para activar la cuenta.

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e9c0d62c-267b-4369-9818-34f4e28f7593" alt="Paso 1" height="230" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/2dc8510f-6032-417a-9bfc-1ef76a250860" alt="Paso 2" height="230" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/0b22feb2-2e75-436a-9ca8-5c2778f52117" alt="Paso 3" height="230" />
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/61bf9cac-90e6-4e40-aeeb-f53b4e5245cd" alt="Paso 4" height="230" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/534ce9c4-016f-4f49-9cd7-703852414ea1" alt="Paso 5" height="230" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/7aa89cc3-18eb-432c-86cd-cfa39875c16b" alt="Paso 6" height="230" />
    </td>
  </tr>
</table>
---

### ☁️ Cuarta parte: Despliegue en AWS

En esta fase se despliega la aplicación en una **instancia EC2 de AWS** y se ejecuta el contenedor Docker publicado en Docker Hub.

#### 1️⃣ Acceso a la máquina virtual
1. Accede a la **instancia EC2** desde AWS Academy Learner Lab (o tu cuenta de AWS).  
2. Conéctate por **SSH** usando el terminal que te proporciona la consola.

#### 2️⃣ Instalación de Docker
Ejecuta los siguientes comandos:

```bash
# Actualizar paquetes
sudo yum update -y
```
# Instalar Docker
```bash
sudo yum install docker
```
---
📸 Evidencias
<table> <tr> <td align="center"> <img src="https://github.com/user-attachments/assets/552b930d-5be1-45fb-ae93-2c01b2cb5eae" alt="AWS Paso 1" height="210" /> </td> <td align="center"> <img src="https://github.com/user-attachments/assets/6a83b995-e10f-4c2f-9b38-c24441090769" alt="AWS Paso 2" height="210" /> </td> <td align="center"> <img src="https://github.com/user-attachments/assets/83ec2262-e7bf-4e4c-9f86-328b6094553d" alt="AWS Paso 3" height="210" /> </td> </tr> <tr> <td align="center"> <img src="https://github.com/user-attachments/assets/995ce2be-9c60-400b-91dd-05d755bf6108" alt="AWS Paso 4" height="210" /> </td> <td align="center"> <img src="https://github.com/user-attachments/assets/127469ee-da7e-4d20-ab54-6ae142d645fc" alt="AWS Paso 5" height="210" /> </td> <td align="center"> <img src="https://github.com/user-attachments/assets/3fe1c84f-9eab-4204-a50d-ef4198cbff5d" alt="AWS Paso 6" height="210" /> </td> </tr> </table> <p align="center"> <img src="https://github.com/user-attachments/assets/67a16068-95d4-4465-8482-980d9f3d8f5c" alt="Resultado final" height="210" /> </p> ```




