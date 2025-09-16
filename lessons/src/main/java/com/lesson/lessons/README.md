# Spring Boot Security - User & Role Management


This is a Spring Boot 3 project demonstrating a robust implementation of user authentication and role-based authorization using Spring Security. The application features two distinct roles: `USER` and `ADMIN`, with different levels of access to API endpoints.

## Features

-   **Authentication:** Secure user login with email and password.
-   **Authorization:** Role-based access control (RBAC).
    -   **ADMIN Role:** Full access to user management (CRUD), system reports, and all user-level functions.
    -   **USER Role:** Access to personal profile, dashboard, and settings.
-   **API Endpoints:** A clear separation of public, user, and admin RESTful endpoints.
-   **Data Persistence:** Uses Spring Data JPA with a MySQL database.
-   **Security Best Practices:**
    -   Password hashing using `BCryptPasswordEncoder`.
    -   CSRF (Cross-Site Request Forgery) protection is enabled.
    -   Centralized exception handling for security and validation errors.
-   **Initial Data:** The application automatically creates default `ADMIN` and `USER` roles, along with two test users on first startup.

## Technologies Used

-   **Java 24**
-   **Spring Boot 3**
-   **Spring Security 6**
-   **Spring Data JPA (Hibernate)**
-   **MySQL**
-   **Maven**

## Prerequisites

-   JDK 24
-   Maven 3.6 or later
-   A running MySQL database instance

## How to Run

1.  **Clone the repository:**
    ```bash
    git clone <lessons-login-managment>
    cd lessons
    ```

2.  **Configure the database:**
    Open the `src/main/resources/application.properties` file and update the following properties to match your MySQL database configuration:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_db_user
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Build and run the application using Maven:**
    ```bash
    mvn spring-boot:run
    ```

4.  The application will start on `http://localhost:8080`.

## Default Users

The application creates two users by default upon startup:

-   **Admin User:**
    -   **Email:** `admin@example.com`
    -   **Password:** `admin123`
    -   **Roles:** `ROLE_ADMIN`, `ROLE_USER`

-   **Regular User:**
    -   **Email:** `user@example.com`
    -   **Password:** `user123`
    -   **Roles:** `ROLE_USER`

## API Endpoints

### Public Endpoints (`permitAll`)

-   `GET /`: Welcome homepage.
-   `POST /auth/register`: Register a new user.
-   `GET /about`: About page.
-   `GET /contact`: Contact page.
-   `/login`: Spring Security's default login endpoint.

### User Endpoints (Requires `USER` or `ADMIN` role)

-   `GET /user/dashboard`: User's personal dashboard.
-   `GET /user/profile`: View current user's profile.
-   `PUT /user/profile/edit`: Edit current user's profile.
-   `GET /user/settings`: User's personal settings.
-   `GET /user/activity`: User's recent activity.

### Admin Endpoints (Requires `ADMIN` role)

-   `GET /admin/dashboard`: Admin dashboard.
-   `GET /admin/users`: Get a list of all users.
-   `POST /admin/users/create`: Create a new user.
-   `PUT /admin/users/{id}/edit`: Edit an existing user by ID.
-   `DELETE /admin/users/{id}/delete`: Delete a user by ID.
-   `GET /admin/roles`: View all available roles.
-   `GET /admin/reports`: View system reports.

### Error Endpoints

-   `GET /access-denied`: Page shown for 403 Forbidden errors.