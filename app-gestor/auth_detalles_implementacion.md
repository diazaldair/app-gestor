# Detalles de la Implementación del Módulo de Autenticación (`auth`)

Este documento especifica a detalle los componentes desarrollados para el módulo de autenticación de **SoloBook Pro**, analizando lo que se tiene actualmente y lo que falta para producción.

---

## 1. Lo que Tenemos Actualmente (Estructura Clean Architecture)

El módulo se diseñó bajo los estándares estrictos de **Clean Architecture** de manera desacoplada:

```text
auth/
├── data/
│   └── datasource/
│       ├── datasource/  --> Fuentes de Datos (Local y Remoto desacoplados)
│       ├── dto/         --> Objetos de transferencia de red (DTOs)
│       ├── mapper/      --> Traductores unidireccionales de datos
│       ├── repository/  --> Implementación concreta del repositorio
│       └── service/     --> Clientes y APIs de red (Firebase Auth)
├── domain/
│   ├── model/           --> Modelos puros del negocio (UserSession)
│   ├── repository/      --> Abstracciones e interfaces del repositorio
│   └── usecase/         --> Casos de uso de negocio (LoginWithEmail)
└── presentation/
    ├── landing/         --> Primer punto de entrada de la aplicación
    └── login/           --> Pantalla de Login de Alta Fidelidad
```

### Componentes Clave:
1.  **MVI en Tres Archivos**:
    *   `LoginUiState`: Estado de formulario inmutable y de solo lectura.
    *   `LoginEvent`: Acciones/intenciones puros de UI (cambios de texto, clicks).
    *   `LoginEfffect`: Efectos colaterales singulares como alertas y navegación.
2.  **Inyección en Koin (`AuthModule.kt`)**: Las capas se instancian a través de Koin de forma perezosa para evitar fugas de memoria o acoplamiento físico directo.
3.  **Persistencia de Sesión Local (`AuthLocalDatasource.kt`)**: Simulación reactiva en memoria utilizando un StateFlow, de manera que la sesión sea accesible para cualquier parte de la app que la solicite de forma asíncrona.

---

## 2. Lo que Falta (Por Implementar para Producción)

Actualmente, para fines de prueba rápida y diseño ágil, muchas integraciones funcionan mediante mocks o simulaciones:

| Componente | Estado Actual | Requerido para Producción |
| :--- | :--- | :--- |
| **Autenticación Firebase** | Simulada a través de `FirebaseManager` guardando textos en base de datos. | Integración del SDK nativo de **Firebase Auth** para gestionar usuarios reales. |
| **Login Social (Google / Apple)** | Simulación con tiempos de retardo (`delay`). | Configuración de credenciales OAuth en Google Cloud Platform y Apple Developer Console. |
| **Persistencia Local Segura** | Guardado temporal en memoria. | Guardado del token cifrado a través de Room cifrado o una base de datos segura de claves (como EncryptedSharedPreferences en Android / Keychain en iOS). |
| **Validación de Formularios** | Validación básica de campos vacíos. | Validaciones con expresiones regulares (Regex) para garantizar correos válidos y contraseñas de alta seguridad (ej. 8 caracteres, números, mayúsculas). |
