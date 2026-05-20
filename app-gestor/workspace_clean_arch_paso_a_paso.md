# Guía: Implementando Clean Architecture para el Setup del Consultorio

En esta guía te explico paso a paso cómo expandimos la pantalla de Perfil Profesional (que inicialmente era solo "Front") para integrarle todas las capas de nuestra Clean Architecture: **Domain** y **Data**.

---

## 1. Lo que teníamos antes (Solo Front-End)
Inicialmente, nuestro `WorkspaceSetupProfileViewModel` solo actualizaba estados visuales y cuando le dábamos "Continuar", simplemente simulaba una espera (`delay(1000)`) y navegaba a la siguiente pantalla sin guardar realmente los datos en ningún lado.
Teníamos MVI, pero la lógica moría ahí.

## 2. Lo que implementamos ahora (El Paso a Paso)

### Paso 1: La Capa de Dominio (Domain)
El **Dominio** es el núcleo de la aplicación. No sabe nada de UI ni de Bases de datos.
1.  **El Modelo**: Creamos `WorkspaceProfile.kt`, una clase pura en Kotlin que define qué es un Perfil Profesional para nuestra regla de negocio.
    ```kotlin
    data class WorkspaceProfile(
        val clinicName: String,
        val fullName: String,
        val specialities: List<String>,
        // ...
    )
    ```
2.  **El Contrato del Repositorio**: Agregamos al `OwnerRepository.kt` (que es una interfaz) la función de guardar:
    ```kotlin
    suspend fun saveWorkspaceProfile(profile: WorkspaceProfile): Result<Unit>
    ```
3.  **El Caso de Uso (UseCase)**: Creamos `SaveWorkspaceProfileUseCase.kt`. Aquí es donde viven las reglas de negocio (ej. validar que los campos no estén vacíos antes de pedirle al repositorio que guarde).

### Paso 2: La Capa de Datos (Data)
Esta capa es responsable de comunicarse con fuentes externas (Firebase, Room).
1.  **El Mapper**: Modificamos `OwnerMapper.kt` añadiendo la función `toFirebaseString` para convertir nuestro modelo de dominio puro `WorkspaceProfile` a un string JSON básico que Firebase entienda.
2.  **El Servicio y Datasource**: 
    *   En `OwnerService.kt`, le dijimos al `FirebaseManager` que guarde en la ruta `workspaces/$uid/profile`.
    *   En `OwnerRemoteDatasource.kt` expusimos esta función.
3.  **El Repositorio Concreto**: Implementamos la función en `OwnerBookingRepository.kt`. Aquí inyectamos el mapper y le enviamos los datos al DataSource remoto. Si hay error, atrapamos la excepción y devolvemos un `Result.failure`.

### Paso 3: Conectando todo con Presentación y Koin (DI)
Finalmente, enlazamos el "Front" que ya teníamos con nuestro nuevo núcleo.
1.  **Inyección de Dependencias**: Registramos nuestro caso de uso `factoryOf(::SaveWorkspaceProfileUseCase)` en `OwnerModule.kt`.
2.  **El ViewModel**: 
    *   Le inyectamos el caso de uso por constructor.
    *   En el evento `OnContinueClicked`, le pasamos todos los datos del `UiState` a nuestro caso de uso.
    *   Esperamos el resultado y, si es `isSuccess`, emitimos el efecto para navegar a la siguiente pantalla.

## 3. Lo que falta (Para Producción)
*   **UID Real del Usuario**: En `OwnerBookingRepository` estamos hardcodeando `"current_user_123"`. En el futuro, esto se conectará al SessionManager real.
*   **Subida Real de Imágenes**: Las fotos en `galleryImages` siguen siendo strings simulados en lugar de URIs locales subiéndose al Storage de Firebase.

¡Listo! Con esto, has logrado que tu arquitectura escale de forma limpia, separando las vistas, la lógica de validación, y las llamadas a base de datos.
