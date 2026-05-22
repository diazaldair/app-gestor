# Desarrollo de Feature: Configuración de Nuevo Servicio (Paso 3 de 3)

## Arquitectura Implementada: Screen-First Isolation

Esta funcionalidad (pantalla para registrar el primer servicio del profesional) ha sido construida bajo el principio de **Screen-First Isolation**. Se ubica exclusivamente en el directorio `owner/setup_service/`. No interfiere con ningún otro flujo, garantizando que el diseño, estado y dependencias sean 100% aislados.

### 1. Capa de Dominio (Domain)
- **`WorkspaceService`**: Modelo puro de Kotlin que representa el servicio, incluyendo `name`, `description`, `price`, `currency` (fijo a USD) y `durationMinutes`.
- **`SetupServiceRepository`**: Interfaz o contrato que abstrae cómo se almacena este servicio.
- **`SaveWorkspaceServiceUseCase`**: Contiene la lógica de negocio, validando que el nombre no esté vacío y que la duración sea mayor a 0 minutos antes de intentar el guardado.

### 2. Capa de Datos (Data)
- **`SetupServiceService`**: Se comunica con Firebase para guardar los datos.
- **`SetupServiceRemoteDatasource`**: Actúa como intermediario puro para aislar la lógica del framework de Firebase del repositorio.
- **`SetupServiceRepositoryImpl`**: Implementa la interfaz del dominio. Transforma (serializa) el `WorkspaceService` en un formato entendible por el backend y delega el guardado.

### 3. Capa de Presentación (MVI)
El patrón **Model-View-Intent (MVI)** se sigue al pie de la letra, usando los principios reactivos:

- **State (`WorkspaceSetupServiceUiState`)**:
  Anotado con `@Immutable` para ayudar a Compose a no recomponer innecesariamente.
  Conserva el estado de todos los inputs (nombre, descripción, precio, moneda) y la selección del tiempo (opción predefinida o tiempo personalizado con horas/minutos).

- **Event (`WorkspaceSetupServiceEvent`)**:
  Sealed interface que mapea cada acción del usuario (cambios de texto, selección de duración predefinida, pulsación de botones +/- para horas y minutos, y los clicks de navegación).

- **Effect (`WorkspaceSetupServiceEfffect`)**:
  Sealed interface con convención de triple "f". Se usa para efectos secundarios como la navegación (NavigateToNextStep, NavigateBack) o mostrar mensajes de error/éxito en Snackbars.

- **ViewModel (`WorkspaceSetupServiceViewModel`)**:
  Consolida el estado. Mantiene un `StateFlow` para el UI y un `SharedFlow` para los efectos. Expone una función `onEvent` que intercepta las acciones del usuario, valida inputs numéricos (para el precio) y delega al caso de uso al presionar "Continuar".

- **UI (`WorkspaceSetupServiceScreen`)**:
  Construida en Jetpack Compose. Implementa un diseño "Dark Glassmorphism" con gradientes de fondo oscuro y campos semitransparentes.
  Incluye la lógica visual para mostrar el selector de duración avanzado solo si el usuario elige la opción "Pers." (Personalizado).

### 4. Inyección de Dependencias (DI)
- **`SetupServiceModule.kt`**: Provee de forma dedicada todas las dependencias (Repository, UseCase, ViewModel y Service) y está registrado en `AppModule.kt`. Ninguna dependencia está compartida globalmente (salvo el `FirebaseManager` subyacente).

---

## Lo que falta por implementar (Next Steps)

Para considerar la feature 100% conectada a nivel global en la App:

1. **Integración con Navegación Global (NavGraph)**:
   Añadir la ruta hacia `WorkspaceSetupServiceScreen` en el gestor de navegación principal (ej. `OwnerNavGraph.kt` o equivalente) para que el "Paso 2 de 3" (Schedule) realmente navegue a este "Paso 3 de 3", y que al finalizar este paso se navegue al `Dashboard`.

2. **Formatos de Precio Dinámicos (Opcional)**:
   Actualmente, el campo de moneda está bloqueado (Disabled) mostrando "USD". Si el proyecto requiere multi-moneda, se debería añadir un `DropdownMenu` en lugar del icono de candado.

3. **Autenticación (Firebase Auth)**:
   Dentro de `SetupServiceRepositoryImpl`, actualmente se usa un ID estático `current_user_123` a modo de *placeholder*. Debería inyectarse o recuperarse el ID real del usuario desde Firebase Auth.
