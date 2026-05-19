# Guía de Aprendizaje: Paso a Paso en la Implementación de Perfil Profesional (MVI & Clean Architecture)

Esta guía documenta detalladamente el flujo de trabajo, orden lógico y conceptos teóricos aplicados en el desarrollo de la pantalla **Perfil Profesional (Paso 1 de 3: Información Básica)**.

---

## 📘 Teoría Clave

### 1. ¿Por qué Clean Architecture con Feature-First?
En proyectos grandes, estructurar el código por "capas globales" (ej. poner todos los ViewModels del proyecto en una sola carpeta `viewmodels/`) genera acoplamiento y conflictos constantes al hacer merges (conflictos de Git).
Con **Feature-First**:
*   Cada funcionalidad (como la configuración inicial del consultorio) está aislada en su propio paquete `owner/presentation/setup`.
*   Un desarrollador puede trabajar en `setup` sin tocar el código de `booking` o `profile`, eliminando casi por completo los conflictos de merge.

### 2. El Flujo de Datos Unidireccional (MVI)
MVI (Model-View-Intent) es un patrón donde la información viaja en una sola dirección:
*   **UiState (El Estado)**: Es una clase inmutable de solo lectura que representa exactamente lo que se ve en la pantalla en un momento dado.
*   **Event (La Intención)**: Cualquier acción del usuario (cambiar un input, presionar un botón, borrar un chip) se emite como un evento estructurado. La UI no modifica variables directamente, solo envía eventos al ViewModel.
*   **Efffect (El Efecto Colateral)**: Son eventos de "un solo disparo" que no representan el estado continuo de la pantalla, como mostrar un mensaje Snackbar o navegar a otra vista.

---

## 🛠️ Paso a Paso de la Implementación en Orden

### Paso 1: Definir el Estado de la UI (`WorkspaceSetupProfileUiState.kt`)
**Por qué primero**: Para modelar una pantalla, debemos entender qué datos retiene.
Estructuramos un estado inmutable para almacenar los campos de texto y listas.

**Código (Fragmento):**
```kotlin
package com.gestorplus.appgestor.owner.presentation.setup.state

import androidx.compose.runtime.Immutable

@Immutable // Anotación clave para optimizar la recomposición en Compose
data class WorkspaceSetupProfileUiState(
    val clinicName: String = "",
    val fullName: String = "",
    val specialities: List<String> = listOf("Cardiologia", "Pediatria"),
    val inputSpeciality: String = "",
    // ... otros campos
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
```

### Paso 2: Definir los Eventos de Interacción (`WorkspaceSetupProfileEvent.kt`)
**Por qué segundo**: Listamos todas las acciones posibles que el usuario puede realizar en la pantalla. Usamos una interfaz sellada (`sealed interface`) para que el compilador nos obligue a manejar todos los casos en el ViewModel.

**Código:**
```kotlin
package com.gestorplus.appgestor.owner.presentation.setup.state

sealed interface WorkspaceSetupProfileEvent {
    data class ClinicNameChanged(val value: String) : WorkspaceSetupProfileEvent
    data class FullNameChanged(val value: String) : WorkspaceSetupProfileEvent
    data class InputSpecialityChanged(val value: String) : WorkspaceSetupProfileEvent
    data object AddSpecialityClicked : WorkspaceSetupProfileEvent
    data class RemoveSpecialityClicked(val speciality: String) : WorkspaceSetupProfileEvent
    // ... demás acciones (OnSubmit, OnBack, etc)
}
```

### Paso 3: Definir los Efectos Colaterales (`WorkspaceSetupProfileEfffect.kt`)
**Por qué tercero**: Declaramos las transiciones fuera del estado ordinario, conocidas como "Side Effects". Un estado es "el input está vacío", un efecto es "Navega a otra pantalla".

**Código:**
```kotlin
package com.gestorplus.appgestor.owner.presentation.setup.state

sealed interface WorkspaceSetupProfileEfffect {
    data object NavigateToServices : WorkspaceSetupProfileEfffect
    data object NavigateBack : WorkspaceSetupProfileEfffect
    data class ShowSnackbar(val message: String) : WorkspaceSetupProfileEfffect
}
```

### Paso 4: Crear el ViewModel (`WorkspaceSetupProfileViewModel.kt`)
**Por qué cuarto**: El ViewModel es el cerebro. Expone el `state` como un `StateFlow` reactivo y procesa eventos inmutables.
*   **`.update { it.copy() }`**: En lugar de hacer `state.value.name = "X"`, copiamos el objeto completo cambiando solo una propiedad. Esto garantiza la inmutabilidad y previene bugs de concurrencia.

**Código (Fragmento):**
```kotlin
fun onEvent(event: WorkspaceSetupProfileEvent) {
    viewModelScope.launch {
        when (event) {
            is WorkspaceSetupProfileEvent.ClinicNameChanged -> {
                _state.update { it.copy(clinicName = event.value, errorMessage = null) }
            }
            WorkspaceSetupProfileEvent.AddSpecialityClicked -> {
                val currentInput = _state.value.inputSpeciality.trim()
                if (currentInput.isNotEmpty()) {
                    _state.update {
                        // Creamos una nueva lista concatenando el nuevo elemento
                        it.copy(
                            specialities = it.specialities + currentInput,
                            inputSpeciality = ""
                        )
                    }
                }
            }
            // ... resto de lógica
        }
    }
}
```

### Paso 5: Desarrollar la Interfaz de Usuario (`WorkspaceSetupProfileScreen.kt`)
**Por qué quinto**: La UI es puramente declarativa. Consume el estado del ViewModel (`state.collectAsState()`) y emite eventos hacia el ViewModel. 

**Componentes Clave Utilizados**:
*   **`LazyRow`**: Usado para la galería de fotos, permite un scroll horizontal altamente optimizado.
*   **`Canvas`**: Lo utilizamos para dibujar las líneas de fuga en perspectiva 3D del mapa, sin usar imágenes pesadas.

**Código (Fragmento del Input de Especialidad):**
```kotlin
OutlinedTextField(
    value = state.inputSpeciality,
    onValueChange = { viewModel.onEvent(WorkspaceSetupProfileEvent.InputSpecialityChanged(it)) },
    placeholder = { Text("Añadir especialidad...") },
    trailingIcon = {
        IconButton(onClick = { viewModel.onEvent(WorkspaceSetupProfileEvent.AddSpecialityClicked) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir")
        }
    }
)
```

### Paso 6: Registrar Dependencias y Enlazar Rutas (`OwnerModule.kt` y `App.kt`)
**Por qué final**: Usamos Inyección de Dependencias (Koin) para crear el ViewModel sin instanciarlo a mano.

**Código (Fragmento `App.kt`):**
```kotlin
Screen.WorkspaceSetupProfile -> {
    WorkspaceSetupProfileScreen(
        onNavigateToNextStep = {
            currentScreen = Screen.DoctorView // Navegación al dashboard
        },
        onNavigateBack = {
            currentScreen = Screen.WorkspaceSetupIntro
        }
    )
}
```
