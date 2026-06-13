# Dental API — Nelumbo Reto Técnico
 
API REST para la gestión de citas odontológicas en múltiples clínicas dentales
 
## Tecnologías utilizadas
 
- **Java 21**
- **Spring Boot 4.1.0**
  - Spring Web
  - Spring Security
  - PostgreSQL Driver
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL** — Motor de base de datos relacional
- **JJWT 0.12.6** — Autenticación basada en tokens JWT
- **Lombok** — Reducción de boilerplate
- **Maven** — Gestión de dependencias y build
## 📁 Estructura del proyecto
 
```
src/main/java/com/nelumbo/dental_api/
├── controller/       # Controladores REST
├── service/          # Lógica de negocio
├── repository/       # Interfaces JPA
├── entity/           # Entidades de persistencia
├── dto/              # Objetos de transferencia de datos
│   ├── auth/
│   ├── clinic/
│   ├── office/
│   ├── dentist/
│   ├── procedure/
│   ├── appointment/
│   ├── notification/
│   └── indicator/
├── security/         # Configuración JWT y Spring Security
├── exception/        # Excepciones personalizadas y handler global
└── config/           # Configuraciones generales
```
 
## Variables de entorno
 
Crea un archivo `.env` o configure las siguientes variables en su entorno local, para poder ejecutar el proyecto
 
```env
DB_URL=jdbc:postgresql://localhost:5432/dental_db
DB_USERNAME=
DB_PASSWORD=
```
 
## Base de datos
 
Motor: **PostgreSQL 18**
 
## Cómo correr el proyecto en local
 
### Prerequisitos
 
- Java 21 instalado
- PostgreSQL corriendo localmente
- Maven (o usar el wrapper `./mvnw` incluido)
### Pasos
 
```bash
# 1. Clonar el repositorio
git clone https://github.com/thoalecorn/detail-api.git
cd detail-api
 
# 2. Configurar variables de entorno
 
# 3. Compilar y ejecutar
./mvnw spring-boot:run
```
 
La API quedará disponible en `http://localhost:8080`.
 
## Usuario administrador precargado
 
Al iniciar la aplicación se crea automáticamente un usuario administrador:
 
```
Email: admin@mail.com
Password: admin
```
 
## Modelo Entidad-Relación

<img width="2756" height="2100" alt="MER dentail-api" src="https://github.com/user-attachments/assets/4b42ab2a-8023-470f-9cf0-d3dbaf6bdc88" />

## Estado del proyecto
 
🚧 En desarrollo.
