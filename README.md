# WeatherApp

Una aplicación de Android moderna para consultar el clima actual de ciudades específicas y también basado en la ubicación del usuario.

## Características

- Consulta del clima actual para las ciudades: Montevideo, Londres, San Pablo, Buenos Aires y Munich
- Obtención del clima según la ubicación GPS del usuario
- Visualización de datos como temperatura actual, mínima y máxima, viento con dirección e imagen descriptiva
- Diseño responsive que se adapta a diferentes tamaños de pantalla y orientaciones
- Soporte para Android 8.0 a 15.0
- Se incorporó la funcionalidad de temas automáticos (claro/oscuro) que reconoce y se adapta al modo de visualización preferido por el usuario en su dispositivo.

## Arquitectura

El proyecto está implementado siguiendo los principios de Clean Architecture con:

- **Capa de Presentación**: UI con Jetpack Compose y ViewModels
- **Capa de Dominio**: Casos de uso e interfaces de repositorio
- **Capa de Datos**: Implementación de repositorios, API y fuentes de datos

## Tecnologías

- **Kotlin**: Lenguaje principal
- **Jetpack Compose**: Framework de UI declarativa
- **MVVM**: Patrón arquitectónico
- **Dagger Hilt**: Inyección de dependencias
- **Retrofit**: Cliente HTTP para consumir la API de OpenWeatherMap
- **Coroutines y Flow**: Para operaciones asíncronas
- **Google Play Services Location**: Para obtener la ubicación del usuario
- **Material 3**: Para componentes y estilos de UI modernos

## Configuración

1. Clona el repositorio:
```
git clone https://github.com/yourusername/weatherapp.git
```

2. Obtén una API key gratuita de [OpenWeatherMap](https://openweathermap.org/api) (En el caso de no querer generar API key ya tiene una)

3. Configura la API key en el archivo `build.gradle.kts` del módulo app:
```kotlin
buildConfigField("String", "WEATHER_API_KEY", "\"TU_API_KEY_AQUI\"")
```

4. Ejecuta la aplicación en Android Studio

## Comentarios

- En dispositivos con Android 8.0 o en emuladores donde la obtención de ubicación puede ser problemática, la aplicación utiliza una ubicación predeterminada para garantizar una experiencia fluida

---
