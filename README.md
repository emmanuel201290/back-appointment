# back-appointment
servicio web del proyecto appointment

#Configuraciones necesarias
JAVA WEB APPLICATION corriendo bajo un contenedor glasfish y base de datos mysql

#Librerias utilizadas
En la carpeta libraries estan las librerias que utiliza el sistema para funcionar, debes importarlas al momento de cargar el sistema

El cors se habilita en el archivo > CorsFilter.java

La clase Init se utiliza para ejecutar el endpoint `https://api.jsonbin.io/b/61b3b51e62ed886f915dd68a` para llenar mis tablas locales. 

En la clase Init tambien hay variables del puerto, usuario y contraseña la cual debes de poner correspondiente a tu configuracion con mysql

El proyecto esta dividido en carpetas EJB, ENTIDADES, SERVICIOS 

# BASE DE DATOS

En el siguiente link puede encontrar el backup de la base de datos y sus tablas mysql `https://res.cloudinary.com/emmanuel201290/raw/upload/v1642388683/bd/clinica_bwrb7b.sql`

Una imagen de mi configuracion local ![This is an image](https://res.cloudinary.com/emmanuel201290/image/upload/v1642388960/bd/configuracion_rc7icg.png)
