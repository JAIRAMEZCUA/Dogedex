# 📚 Manual: Coroutines y Suspend Functions

## Introducción

Las **Coroutines** permiten ejecutar operaciones asincrónicas sin bloquear el hilo principal. Las **Suspend Functions** son funciones que se pueden pausar y reanudar.

---

## Conceptos Clave

### Suspend Function
Una función que se puede **pausar** y **reanudar**:

```kotlin
suspend fun fetchDogs(): List<Dog> {
    // Esta función puede pausarse aquí
    delay(2000) // No bloquea el hilo principal
    // Y reanudarse aquí
    return listOf(Dog(1, "Bulldog", ...))
}
```

**Importante**: Solo pueden ser llamadas desde:
- Otra suspend function
- Una corrutina (launch, async, etc.)

---

### Dispatchers - Dónde se ejecuta

#### Dispatchers.Main
- **Uso**: Actualizar UI
- **Hilo**: Hilo principal

```kotlin
viewModelScope.launch(Dispatchers.Main) {
    binding.textView.text = "Actualizar UI"
}
```

#### Dispatchers.IO
- **Uso**: Operaciones de red, BD
- **Hilo**: Hilo secundario (optimizado para I/O)

```kotlin
withContext(Dispatchers.IO) {
    val data = fetchFromNetwork()  // No bloquea main
}
```

#### Dispatchers.Default
- **Uso**: Cálculos pesados
- **Hilo**: Hilo secundario (optimizado para CPU)

```kotlin
withContext(Dispatchers.Default) {
    val resultado = calcularDatos()
}
```

---

## Estructura Básica

### Suspend Function en Repository

```kotlin
class DogRepository {
    // ⭐ Función SUSPEND
    suspend fun fetchDogs(): List<Dog> = withContext(Dispatchers.IO) {
        // Hilo secundario (no bloquea)
        delay(2000)
        
        // Simular llamada a API
        return@withContext listOf(
            Dog(1, "Bulldog", "...", "40kg"),
            Dog(2, "Labrador", "...", "35kg")
        )
    }
}
```

### Llamar desde ViewModel

```kotlin
class DogViewModel : ViewModel() {
    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> = _dogs
    
    fun loadDogs() {
        // ⭐ viewModelScope.launch = Corrutina en contexto de ViewModel
        viewModelScope.launch {
            try {
                // ⭐ Llamar suspend function
                val dogsFromApi = repository.fetchDogs()
                // Aquí estamos en hilo PRINCIPAL automáticamente
                
                _dogs.value = dogsFromApi  // Actualizar UI seguro
                
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
```

### Observar desde Activity

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: DogViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel.dogs.observe(this) { dogs ->
            // Automáticamente se actualiza cuando viewModel._dogs.value cambia
            adapter.submitList(dogs)
        }
        
        viewModel.loadDogs()  // Inicia la corrutina
    }
}
```

---

## Flujo Paso a Paso

```
1️⃣  Activity.onCreate()
    ↓
2️⃣  viewModel.loadDogs()  (función normal)
    ↓
3️⃣  viewModelScope.launch  (crear corrutina)
    ↓
4️⃣  repository.fetchDogs()  (suspend function)
    ↓
5️⃣  withContext(Dispatchers.IO)  (cambiar a hilo IO)
    ↓
6️⃣  delay(2000)  (esperar SIN bloquear hilo principal)
    ↓
7️⃣  Retornar datos  (automáticamente en hilo principal)
    ↓
8️⃣  _dogs.value = datos  (actualizar LiveData)
    ↓
9️⃣  LiveData notifica cambio
    ↓
🔟 Activity.observe() recibe datos
    ↓
1️⃣1️⃣ UI se actualiza ✅
```

---

## Corrutinas - launch vs async

### launch
- **Retorna**: Job (no se puede acceder al resultado)
- **Uso**: Operaciones que no necesitan retorno

```kotlin
viewModelScope.launch {
    repository.fetchDogs()  // No accedemos al resultado
    // Hacer algo más
}
```

### async
- **Retorna**: Deferred<T> (se puede acceder al resultado)
- **Uso**: Obtener resultado y hacer algo con él

```kotlin
viewModelScope.launch {
    val dogsDeferred = async { repository.fetchDogs() }
    val dogs = dogsDeferred.await()  // Esperar resultado
    _dogs.value = dogs
}
```

---

## Manejo de Errores

```kotlin
viewModelScope.launch {
    try {
        _isLoading.value = true
        val dogs = repository.fetchDogs()
        _dogs.value = dogs
        
    } catch (e: IOException) {
        _error.value = "Error de red"
    } catch (e: Exception) {
        _error.value = "Error desconocido"
    } finally {
        _isLoading.value = false
    }
}
```

---

## Operaciones Comunes

### Obtener datos de API

```kotlin
suspend fun fetchDogs(): List<Dog> = withContext(Dispatchers.IO) {
    delay(2000)  // Simular red
    return@withContext listOf(...)
}
```

### Acceder a Base de Datos

```kotlin
suspend fun getDogsFromDB(): List<Dog> = withContext(Dispatchers.IO) {
    return@withContext database.dogDao().getAll()
}
```

### Cálculo Pesado

```kotlin
suspend fun processImages(): List<Bitmap> = withContext(Dispatchers.Default) {
    return@withContext images.map { compressImage(it) }
}
```

### Actualizar UI

```kotlin
viewModelScope.launch(Dispatchers.Main) {
    binding.textView.text = "Datos cargados"
}
```

---

## viewModelScope

**¿Qué es?** Un CoroutineScope vinculado al ciclo de vida del ViewModel

**Ventajas:**
- ✅ Las corrutinas se cancelan automáticamente cuando ViewModel se destruye
- ✅ No necesitas manejar manualmente el ciclo de vida
- ✅ Seguro contra memory leaks

```kotlin
fun loadDogs() {
    viewModelScope.launch {
        // Si el ViewModel se destruye, esta corrutina se cancela
        val dogs = repository.fetchDogs()
        _dogs.value = dogs
    }
}
// Activity se cierra → ViewModel se destruye → corrutina se cancela ✅
```

---

## Estructura Recomendada

### Repository
```kotlin
class DogRepository {
    suspend fun fetchDogs(): List<Dog> = withContext(Dispatchers.IO) {
        // Llamada a API/BD aquí
    }
}
```

### ViewModel
```kotlin
class DogViewModel(private val repository: DogRepository) : ViewModel() {
    fun loadDogs() {
        viewModelScope.launch {
            try {
                val dogs = repository.fetchDogs()
                _dogs.value = dogs
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
```

### Activity
```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: DogViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel.dogs.observe(this) { dogs ->
            // Actualizar UI
        }
        
        viewModel.loadDogs()
    }
}
```

---

## Diagrama Visual

```
HILO PRINCIPAL (UI)          HILO SECUNDARIO (IO)
     │                              │
     ├─ viewModel.loadDogs()        │
     │                              │
     ├─ launch {                    │
     │    fetchDogs() ──────────────┼──→ withContext(IO)
     │    ↓ (pausa aquí)            │    ↓
     │                              │    delay(2000)
     │                              │    ↓
     │                              │    return datos
     │    ← (resume aquí) ──────────┼── datos retorna
     │    ↓                         │
     │    _dogs.value = datos       │
     │    ↓                         │
     │ LiveData notifica            │
     │    ↓                         │
     ├─ observe() recibe datos      │
     │    ↓                         │
     └─ UI se actualiza ✅          │
```

---

## Dependencias

```gradle
dependencies {
    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0"
    
    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0"
}
```

---

## Tips Importantes

- ✅ Usar `viewModelScope` (automáticamente cancela corrutinas)
- ✅ `withContext(Dispatchers.IO)` para operaciones de red/BD
- ✅ `withContext(Dispatchers.Main)` para actualizar UI (raro, launch ya lo hace)
- ✅ Siempre usar `try-catch` en suspend functions
- ✅ Las suspend functions son **seguras** (no bloquean el hilo principal)

---

## Leer Más

Para más detalles, consulta el [README principal](../README.md#coroutines-y-suspend-functions)

