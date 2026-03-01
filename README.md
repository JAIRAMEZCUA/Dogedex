# DogeDex - Documentación

## 📖 Manuales Disponibles

Esta documentación está organizada en manuales separados y modularizados para fácil navegación:

### 1. 📱 [Manual de RecyclerView](./docs/MANUAL_RECYCLERVIEW.md)
Aprende a implementar listas eficientes que reutilizan vistas.

**Temas cubiertos:**
- ¿Qué es RecyclerView y por qué se llama "Recicler"?
- Los 5 métodos principales
- Implementación paso a paso
- Concepto de reciclado de vistas

### 2. 🏗️ [Manual de MVVM](./docs/MANUAL_MVVM.md)
Arquitectura moderna para separar UI de lógica de negocio.

**Temas cubiertos:**
- Model, View, ViewModel explicado
- LiveData vs StateFlow vs MutableLiveData
- Flujo de datos en MVVM
- Implementación completa

### 3. ⚡ [Manual de Coroutines](./docs/MANUAL_COROUTINES.md)
Operaciones asincrónicas sin bloquear el hilo principal.

**Temas cubiertos:**
- Suspend Functions
- Dispatchers (Main, IO, Default)
- Manejo de errores
- viewModelScope

### 4. 🏗️ [Manual de Arquitectura MVVM + Clean Architecture](./docs/MANUAL_ARQUITECTURA_MVVM_CLEAN.md)
Estructura profesional de proyecto con Clean Architecture.

**Temas cubiertos:**
- Las 4 capas (Presentation, Domain, Data, DI)
- Estructura de carpetas completa
- Flujo de datos paso a paso
- Ejemplo completo de implementación

### 5. 📁 [Diferencia: model/ vs network/](./docs/DIFERENCIA_MODEL_VS_NETWORK.md)
Explicación sencilla de la diferencia entre carpetas de datos.

**Temas cubiertos:**
- Qué es data/model/ (Entidades de BD - Room)
- Qué es data/network/ (DTOs de API - Retrofit)
- Comparativa visual y tabla
- Ejemplo real paso a paso

---

## 🎯 Inicio Rápido

Selecciona el tema que deseas aprender:

- **Principiante**: Comienza con el [Manual de RecyclerView](./docs/MANUAL_RECYCLERVIEW.md)
- **Intermedio**: Aprende [MVVM](./docs/MANUAL_MVVM.md) después
- **Avanzado**: Domina [Coroutines](./docs/MANUAL_COROUTINES.md)

---

## 🔗 Estructura del Proyecto

```
app/src/main/java/com/example/dogedex/
├── model/
│   └── Dog.kt
├── repository/
│   └── DogRepository.kt
├── viewmodel/
│   └── DogViewModel.kt
├── adapter/
│   └── DogAdapter.kt
└── MainActivity.kt
```

---

## 📚 Documentación Adicional

Archivos de referencia rápida en la raíz del proyecto:
- `RESUMEN_RAPIDO_METODOS.md` - Resumen de 30 minutos
- `TARJETAS_REFERENCIA_RAPIDA.md` - Flashcards imprimibles
- `INDICE_DOCUMENTACION.md` - Índice completo
- `RECYCLERVIEW_FLUJO_PASO_A_PASO.md` - Flujo detallado paso a paso

---

## 💡 Concepto Clave

**RecyclerView + MVVM + Coroutines** es la combinación moderna para:
- ✅ Mostrar listas eficientes
- ✅ Separar responsabilidades
- ✅ Operaciones asincrónicas seguras

---

## 📞 ¿Necesitas Ayuda?

Cada manual tiene:
- Ejemplos de código completo
- Diagramas visuales
- Tips y mejores prácticas
- Enlaces entre documentos

**Selecciona el manual que necesitas y comienza! 🚀**
