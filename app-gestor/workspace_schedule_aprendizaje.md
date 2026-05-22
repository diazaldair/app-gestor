# Guía de Aprendizaje: Implementación de la Pantalla "Horarios y Disponibilidad"

Esta guía explica **paso a paso** y de manera teórica cómo y por qué se implementó el **Paso 2 de 3: Horarios y Disponibilidad** siguiendo los principios de **Clean Architecture** y **MVI (Model-View-Intent)** en Android/Kotlin Multiplatform (KMP).

---

## 1. Modificación de la Capa de Datos (Data Layer)
*¿Por qué empezar aquí?* En Clean Architecture, la capa de datos es la encargada de saber **cómo** y **dónde** se guarda la información (API, Firebase, Room). Antes de crear interfaces de usuario, necesitamos estar seguros de que podemos almacenar la información recolectada.

### A. Servicio (OwnerService.kt)
**Concepto:** El *Service* es el componente que interactúa directamente con clientes de red (en este caso Firebase a través del `FirebaseManager`).
**Código añadido:**
```kotlin
suspend fun saveWorkspaceSchedule(uid: String, data: String) {
    firebaseManager.saveData("workspaces/$uid/schedule", data)
}
```
*Aquí definimos el "endpoint" exacto de la base de datos NoSQL donde se guardará.*

### B. Datasource (OwnerRemoteDatasource.kt)
**Concepto:** Los *Datasources* abstraen el servicio subyacente. Si mañana cambiamos Firebase por Retrofit (API REST), los repositorios que usan este Datasource no se enterarán del cambio.
**Código añadido:**
```kotlin
suspend fun saveWorkspaceSchedule(uid: String, data: String) {
    ownerService.saveWorkspaceSchedule(uid, data)
}
```

### C. Repositorio (OwnerBookingRepository.kt)
**Concepto:** El *Repository* consolida las fuentes de datos (Remote y Local). Es la única fuente de verdad para el dominio (Domain). Su función recibe el modelo de dominio (`WorkspaceSchedule`), lo serializa (prepara para enviar) y usa el `RemoteDatasource`.
**Código añadido:**
```kotlin
override suspend fun saveWorkspaceSchedule(schedule: WorkspaceSchedule): Result<Unit> {
    return try {
        // Formateamos la lista a string, ej: "L,M,X|08:00|13:00|15:00|20:00"
        val daysStr = schedule.workingDays.joinToString(",")
        val dataString = "$daysStr|${schedule.morningStart}|${schedule.morningEnd}|${schedule.afternoonStart}|${schedule.afternoonEnd}"
        val currentUid = "current_user_123" // TO-DO: Obtener desde Auth
        
        remoteDatasource.saveWorkspaceSchedule(currentUid, dataString)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

---

## 2. Inyección de Dependencias (OwnerModule.kt)
*¿Por qué hacer esto?* En Kotlin, usamos **Koin** (o Hilt/Dagger) para que el sistema construya los objetos automáticamente y pase las dependencias necesarias al ViewModel o a los UseCases (por ejemplo, el repositorio al caso de uso).

**Código añadido:**
```kotlin
factoryOf(::SaveWorkspaceScheduleUseCase)
viewModelOf(::WorkspaceSetupScheduleViewModel)
```
- `factoryOf`: Crea una nueva instancia del caso de uso cada vez que se requiere.
- `viewModelOf`: Le dice a Koin que este es un ViewModel y lo mantenga atado al ciclo de vida de la pantalla.

---

## 3. Capa de Presentación (Patrón MVI)
El patrón **MVI** (Model-View-Intent) define un flujo de datos unidireccional: La UI emite Eventos (Intent), el ViewModel actualiza el Estado (Model) y reacciona emitiendo un Efecto (Side Effect) o un nuevo Estado que la UI (View) refleja automáticamente.

### A. El Estado (WorkspaceSetupScheduleUiState.kt)
**Concepto:** Representa **absolutamente todo lo que se ve en la pantalla**. Si la UI necesita mostrar algo, debe estar aquí.
```kotlin
import androidx.compose.runtime.Immutable

@Immutable
data class WorkspaceSetupScheduleUiState(
    val selectedDays: List<String> = emptyList(), 
    val morningStart: String = "08:00",
    // ... otros campos ...
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

### B. Eventos o Acciones (WorkspaceSetupScheduleEvent.kt)
**Concepto:** Todo lo que el usuario puede *hacer* en la pantalla. Esto evita tener decenas de funciones sueltas en el ViewModel.
```kotlin
sealed interface WorkspaceSetupScheduleEvent {
    data class DayToggled(val day: String) : WorkspaceSetupScheduleEvent
    object OnContinueClicked : WorkspaceSetupScheduleEvent
    // ... otros ...
}
```

### C. Efectos Secundarios (WorkspaceSetupScheduleEfffect.kt)
**Concepto:** Eventos de un solo uso (One-shot events) que no deben persistir si el usuario gira la pantalla. El manual estipula usar triple "f" (`Efffect`) y una interfaz sellada (`sealed interface`) para diferenciarlos del estado.
```kotlin
sealed interface WorkspaceSetupScheduleEfffect {
    object NavigateToNextStep : WorkspaceSetupScheduleEfffect
    // ...
}
```

### D. El ViewModel (WorkspaceSetupScheduleViewModel.kt)
**Concepto:** El cerebro de la pantalla. Mantiene el Estado, procesa los Eventos, ejecuta la lógica de negocio (UseCases) y emite Efectos.
**Puntos clave del código:**
1. **Manejo del estado reactivo:** Utilizamos `MutableStateFlow` (variable interna mutable) y la exponemos como `StateFlow` (variable pública solo lectura).
2. **Función `onEvent`:** Un bloque `when` gigante que redirige las acciones del usuario a funciones privadas.
3. **Validación:** Antes de llamar a `SaveWorkspaceScheduleUseCase`, revisa si se escogieron días. Si no, actualiza el `state.errorMessage`.

### E. La Interfaz en Jetpack Compose (WorkspaceSetupScheduleScreen.kt)
**Concepto:** Código puramente declarativo. Se reconstruye a sí mismo automáticamente cada vez que el `state` del ViewModel cambia.
**Conceptos aplicados:**
- **`collectAsState()`:** Escucha los cambios del ViewModel reactivamente.
- **`LaunchedEffect`:** Un bloque corrutina que está "escuchando" los `Effect` (navegación). Evita que la navegación se ejecute múltiples veces al rotar el dispositivo.
- **UI Estilizada:** Utiliza `Brush.verticalGradient` para el fondo nocturno y `Modifier.clip()` junto con colores `alpha = 0.7f` para imitar el "Glassmorphism" y el aspecto premium del Paso 1.
- **Componentes Clickeables:** Los días (L, M, X...) no son botones clásicos, sino simples `Box` de Compose que usan `Modifier.clickable { viewModel.onEvent(DayToggled(day)) }`. Al dar click, el evento viaja al ViewModel, el ViewModel añade el día al State, el State cambia y Compose vuelve a dibujar el `Box` poniéndolo azul. Todo en milisegundos.

---
**Conclusión de Aprendizaje:** 
Al seguir este orden de implementación, aseguramos que la capa visual (Compose) esté totalmente desacoplada del origen de los datos (Firebase). Si algún día la API cambia, la UI no sufrirá ninguna modificación; solo se ajustará el *Repository*.
