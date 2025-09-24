# Imagen base con JDK 21
FROM eclipse-temurin:21-jre

# Directorio de trabajo dentro del contenedor
WORKDIR /usrapp/bin

# Puerto que expondrá la app (interno)
ENV PORT=6000

# Copiar el fat-jar generado por Maven
COPY target/restNoSpring-1.0-SNAPSHOT.jar app.jar

# Comando para arrancar la aplicación
CMD ["java","-jar","app.jar"]
