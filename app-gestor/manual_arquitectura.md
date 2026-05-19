# Manual de Arquitectura y Guía de Estructura de Desarrollo
**Proyecto:** App-Gestor (Kotlin Multiplatform)

Este documento detalla el estándar de arquitectura adoptado en **App-Gestor**. Está diseñado para alinearse perfectamente con el estándar de robustez de producción y servir de manual oficial para todo el equipo de desarrollo, evitando solapamientos y minimizando conflictos de fusión (*merge conflicts*) en Git.

---

## 1. Principios de la Arquitectura

Nuestra arquitectura de software combina **Clean Architecture** con un enfoque **Feature-First / Screen-First** (Funcionalidad/Pantalla primero), asistida por los patrones de presentación **MVI (Model-View-Intent)** y **MVVM (Model-View-ViewModel)**.

### Pilares Fundamentales:
1. **Screen-First Isolation (Aislamiento por Pantalla)**: Cada pantalla o flujo de negocio principal se organiza en una carpeta autocontenida que contiene sus propias capas de datos, dominio y presentación.
2. **Unidirectional Data Flow (MVI - Flujo de Datos Unidireccional)**: La UI solo reacciona al estado expuesto por el ViewModel y emite eventos puros para cualquier interacción del usuario.
3. **Offline-First Persistence**: El repositorio local (Room) actúa como la única fuente de verdad (*Single Source of Truth*) para la UI, sincronizándose en segundo plano con la base de datos remota (Firebase Realtime Database).
4. **Inyección de Dependencias Modular**: Cada funcionalidad declara sus dependencias en un archivo `*Module.kt` en el paquete global `di` utilizando **Koin**.

---

## 2. Estructura de Directorios Estándar

Cada módulo/funcionalidad de la aplicación debe reproducir con total precisión la siguiente jerarquía de archivos:

```text
feature-name/
│
├── data/
│   └── datasource/
│       ├── datasource/       # Proveedores de datos locales y remotos independientes
│       │   ├── FeatureLocalDatasource.kt   # Encapsula Room DAOs
│       │   └── FeatureRemoteDatasource.kt  # Encapsula APIs y servicios remotos
│       │
│       ├── dto/              # Objetos de Transferencia de Datos (schemas de red/BD)
│       │   └── FeatureDto.kt
│       │
│       ├── mapper/           # Mapeadores de datos (Entity/DTO <-> Domain Model)
│       │   └── FeatureMapper.kt
│       │
│       ├── repository/       # Implementación concreta del repositorio de dominio
│       │   └── FeatureRepositoryImpl.kt
│       │
│       └── service/          # Clientes de red directos (Firebase, Ktor, etc.)
│           └── FeatureService.kt
│
├── domain/
│   ├── model/                # Entidades puras de negocio (Kotlin puro, sin frameworks)
│   │   └── FeatureModel.kt
│   │
│   ├── repository/           # Contrato/Interface del repositorio
│   │   └── FeatureRepository.kt
│   │
│   └── usecase/              # Casos de uso de propósito único (Single Responsibility)
│       ├── GetFeatureUseCase.kt
│       └── UpdateFeatureUseCase.kt
│
└── presentation/
    ├── composable/           # Componentes visuales pequeños y reutilizables
    │   └── FeatureItemComponent.kt
    │
    ├── screen/               # Pantallas Composable principales de la UI
    │   └── FeatureScreen.kt
    │
    ├── state/                # Contratos MVI físicamente separados
    │   ├── FeatureUiState.kt # Estado inmutable de la pantalla (exclusivo sufijo 'UiState')
    │   ├── FeatureEvent.kt   # Acciones intencionales del usuario (sufijo 'Event')
    │   └── FeatureEfffect.kt # Efectos secundarios persistentes/singulares (triple 'f')
    │
    └── viewmodel/            # ViewModel que expone el estado y procesa eventos
        └── FeatureViewModel.kt
```

---

## 3. Guía de Responsabilidades por Capa

### Capa de Presentación (`presentation/`)
* **`FeatureUiState.kt`**: Debe ser una clase de datos anotada con `@Immutable` (Compose runtime) que contenga solo valores primitivos o modelos de dominio. Evitar lógica interna.
* **`FeatureEvent.kt`**: Interfaz sellada (`sealed interface`) que enumera todas las intenciones del usuario sobre la UI (ej. clics en botones, entrada de texto).
* **`FeatureEfffect.kt`**: Interfaz sellada con triple **f** (`Efffect`) para eventos de un único disparo como navegación, visualización de Snacks o Alertas Toast.
* **`FeatureViewModel.kt`**: Hereda de `ViewModel()` de KMP. Mantiene un `MutableStateFlow` de `UiState` y un `MutableSharedFlow` de `Efffect`. Expone funciones públicas únicamente a través del método `onEvent(event: FeatureEvent)`.

### Capa de Dominio (`domain/`)
* **`FeatureModel.kt`**: Modelos de datos en Kotlin puro, totalmente independientes de Room o Firebase.
* **`FeatureRepository.kt`**: Interfaz que define las operaciones lógicas requeridas por los casos de uso.
* **`usecase/`**: Clases con una sola función ejecutable (`operator fun invoke(...)`) que aíslan las reglas de negocio globales de la aplicación.

### Capa de Datos (`data/datasource/...`)
* **`FeatureService.kt`**: Gestiona las operaciones de red directas o persistencias remotas (Firebase, Ktor).
* **`FeatureRemoteDatasource.kt`**: Proporciona los datos del servicio de red hacia el repositorio.
* **`FeatureLocalDatasource.kt`**: Lee y escribe directamente de los Room DAOs compartidos.
* **`FeatureMapper.kt`**: Traduce DTOs a modelos de dominio, y entidades locales a dominio para asegurar el desacoplamiento total de dependencias.
* **`FeatureRepositoryImpl.kt`**: Coordina el flujo offline-first: lee localmente, actualiza la base de datos local y sincroniza asíncronamente en segundo plano con la nube.

---

## 4. Estándar de Inyección de Dependencias (Koin)

Para añadir una nueva funcionalidad al sistema de inyección, se debe crear un archivo en el paquete global `di` (ej. `composeApp/src/commonMain/kotlin/com/gestorplus/appgestor/di/`):

```kotlin
// composeApp/src/commonMain/kotlin/com/gestorplus/appgestor/di/FeatureModule.kt
package com.gestorplus.appgestor.di

import com.gestorplus.appgestor.feature.data.datasource.datasource.*
import com.gestorplus.appgestor.feature.data.datasource.mapper.FeatureMapper
import com.gestorplus.appgestor.feature.data.datasource.repository.FeatureRepositoryImpl
import com.gestorplus.appgestor.feature.data.datasource.service.FeatureService
import com.gestorplus.appgestor.feature.domain.repository.FeatureRepository
import com.gestorplus.appgestor.feature.domain.usecase.*
import com.gestorplus.appgestor.feature.presentation.viewmodel.FeatureViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureModule = module {
    // 1. Mappers y Servicios
    single { FeatureMapper() }
    single { FeatureService(get()) }
    
    // 2. Fuentes de Datos (Datasources)
    single { FeatureLocalDatasource(get()) }
    single { FeatureRemoteDatasource(get()) }
    
    // 3. Repositorio
    single<FeatureRepository> { FeatureRepositoryImpl(get(), get(), get()) }
    
    // 4. Casos de Uso (Usecases)
    factoryOf(::GetFeatureUseCase)
    factoryOf(::UpdateFeatureUseCase)
    
    // 5. ViewModel
    viewModelOf(::FeatureViewModel)
}
```

Posteriormente, el archivo debe agregarse al agregador global en `AppModule.kt`:
```kotlin
val appModules = listOf(
    databaseModule,
    bookingModule,
    ownerModule,
    profileModule,
    featureModule // Añadido aquí
)
```

---

## 5. Reglas de Git y Trabajo en Equipo

Seguir estas reglas es **obligatorio** para todos los integrantes del equipo técnico para evitar conflictos de fusión (*merge conflicts*):

1. **Trabajar únicamente en ramas aisladas por funcionalidad**:
   * Estructura de rama: `feature/nombre-de-la-pantalla` (ej. `feature/booking`, `feature/profile`).
2. **Prohibido tocar archivos de otra funcionalidad**:
   * Si trabajas en el flujo de `booking`, bajo ninguna circunstancia debes editar archivos dentro de `owner` o `profile`. Las carpetas están estrictamente aisladas.
3. **Puntos de contacto compartidos controlados**:
   * El archivo de rutas de navegación `App.kt` y el agregador `AppModule.kt` son los únicos archivos compartidos del proyecto. Al realizar cambios en estos, realiza commits pequeños e inmediatos para evitar desalineaciones rápidas en Git.
4. **Respetar la nomenclatura estrictamente**:
   * Sufijo de estado: `UiState` (ej. `ProfileUiState.kt`).
   * Sufijo de eventos: `Event` (ej. `ProfileEvent.kt`).
   * Sufijo de efectos: `Efffect` con **triple f** (ej. `ProfileEfffect.kt`).
