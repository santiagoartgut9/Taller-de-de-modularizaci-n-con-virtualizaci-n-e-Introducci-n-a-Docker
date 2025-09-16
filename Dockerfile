# Imagen base con JDK 21
FROM eclipse-temurin:21-jre

# Directorio de trabajo dentro del contenedor
WORKDIR /usrapp/bin

# Puerto que expondrá la app (interno)
ENV PORT 6000

# Copiar clases compiladas y dependencias
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

# Comando para arrancar Spring Boot
CMD ["java","-cp","./classes:./dependency/*","co.edu.escuelaing.RestServiceApplication"]
