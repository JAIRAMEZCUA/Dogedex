# 📚 Manual: MVVM Architecture

## Introducción

**MVVM** (Model-View-ViewModel) es un patrón arquitectónico que separa las responsabilidades:

- **Model**: Datos y lógica (base de datos, API)
- **View**: UI (Activity, Fragment)
- **ViewModel**: Conecta Model y View, maneja lógica de presentación

---

## Ventajas de MVVM

✅ **Separación de responsabilidades** - Cada componente tiene un propósito claro  
✅ **Fácil de testear** - Lógica separada de la UI  
✅ **Reutilizable** - ViewModel persiste en cambios de configuración  
✅ **Mantenible** - Código más organizado y limpio  

---

## Componentes Principales

### 1️⃣ Model - Datos y Lógica

```kotlin
// Dog.kt - Data Class
data class Dog(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val weight: String
)
```

```kotlin
// DogRepository.kt - Acceso a datos
class DogRepository {
    suspend fun fetchDogs(): List<Dog> {
        return withContext(Dispatchers.IO) {
            delay(2000) // Simular API call
            listOf(
                Dog(1, "Bulldog", "...", "40kg"),
                Dog(2, "Labrador", "...", "35kg")
            )
        }
    }
}
```

---

### 2️⃣ ViewModel - Lógica de Presentación

```kotlin
// DogViewModel.kt
class DogViewModel(
    private val repository: DogRepository = DogRepository()
) : ViewModel() {
    
    // LiveData privado (solo escritura en ViewModel)
    private val _dogs = MutableLiveData<List<Dog>>()
    // LiveData público (solo lectura en Activity)
    val dogs: LiveData<List<Dog>> = _dogs
    
    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Manejo de errores
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    /**
     * Función que lanza corrutina en viewModelScope
     * Se cancela automáticamente al destruir ViewModel
     */
    fun loadDogs() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Llamar suspend function (hilo secundario)
                val dogsFromApi = repository.fetchDogs()
                
                // Actualizar LiveData (hilo principal automático)
                _dogs.value = dogsFromApi
                
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectDog(dog: Dog) {
        // Lógica de selección
        println("Perro seleccionado: ${dog.name}")
    }
}
```

---

### 3️⃣ View - Activity

```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    // ViewModel se crea automáticamente
    private val viewModel: DogViewModel by viewModels()
    private lateinit var dogAdapter: DogAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        setupRecyclerView()
        observeViewModel()
        viewModel.loadDogs()
    }
    
    private fun setupRecyclerView() {
        dogAdapter = DogAdapter { dog ->
            viewModel.selectDog(dog)
        }
        binding.dogsRecyclerView.adapter = dogAdapter
    }
    
    /**
     * ⭐ OBSERVAR cambios del ViewModel
     * Cuando el ViewModel actualiza, la Activity REACCIONA
     */
    private fun observeViewModel() {
        
        // Observar lista de perros
        viewModel.dogs.observe(this) { dogs ->
            dogAdapter.submitList(dogs)
        }
        
        // Observar estado de carga
        viewModel.isLoading.observe(this) { isLoading ->
            binding.loadingProgressBar.visibility = 
                if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Observar errores
        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
```

---

## LiveData vs StateFlow vs MutableLiveData

### LiveData
- **Uso**: Observar datos en Activity/Fragment
- **Ciclo de vida**: Activity-aware (automático)
- **Thread safe**: Sí
- **Corrutinas**: No nativa

```kotlin
val dogs: LiveData<List<Dog>> = _dogs
dogs.observe(this) { dogsData -> 
    // Actualizar UI
}
```

### StateFlow
- **Uso**: Streams reactivos con corrutinas
- **Ciclo de vida**: Manual (necesita lifecycle.repeatOnLifecycle)
- **Thread safe**: Sí
- **Corrutinas**: Diseñado para corrutinas

```kotlin
val dogs: StateFlow<List<Dog>> = _dogs.asStateFlow()
lifecycleScope.launch {
    dogs.collect { dogsData ->
        // Actualizar UI
    }
}
```

### MutableLiveData
- **Uso**: Versión mutable de LiveData
- **Propósito**: Permitir cambios en el valor
- **Pattern**: Privado en ViewModel, público en Activity (solo lectura)

```kotlin
private val _dogs = MutableLiveData<List<Dog>>()
val dogs: LiveData<List<Dog>> = _dogs

_dogs.value = newDogs  // Actualizar (solo en ViewModel)
```

---

## Flujo de Datos en MVVM

```
Activity                ViewModel              Repository
   │                       │                       │
   ├─ onCreate() ─────────→ loadDogs()            │
   │                       │                       │
   │                       ├─ viewModelScope       │
   │                       │  .launch              │
   │                       │                       │
   │                       ├─ fetchDogs() ────────→ Hilo IO
   │                       │  (suspend)            │
   │                       │                    Delay 2000ms
   │                       │                       │
   │                       │←────── datos ─────────┤
   │                       │                       │
   │                       ├─ _dogs.value = datos  │
   │                       │  (notifica cambio)    │
   │                       │                       │
   │←── dogs.observe() ─────┤                      │
   │   (recibe datos)       │                      │
   │                       │                       │
   ├─ dogAdapter.submitList()                     │
   │ (RecyclerView actualizado)                   │
   │                       │                       │
   └─── UI renderizada ✅                         │
```

---

## Implementación Paso a Paso

### Paso 1: Crear Model
```kotlin
data class Dog(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val weight: String
)
```

### Paso 2: Crear Repository
```kotlin
class DogRepository {
    suspend fun fetchDogs(): List<Dog> = withContext(Dispatchers.IO) {
        delay(2000)
        listOf(
            Dog(1, "Bulldog", "...", "40kg"),
            Dog(2, "Labrador", "...", "35kg")
        )
    }
}
```

### Paso 3: Crear ViewModel
```kotlin
class DogViewModel(
    private val repository: DogRepository = DogRepository()
) : ViewModel() {
    
    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> = _dogs
    
    fun loadDogs() {
        viewModelScope.launch {
            try {
                val dogsFromApi = repository.fetchDogs()
                _dogs.value = dogsFromApi
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}
```

### Paso 4: Observar en Activity
```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: DogViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel.dogs.observe(this) { dogs ->
            // Actualizar UI con nuevos datos
        }
        
        viewModel.loadDogs()
    }
}
```

---

## Ciclo de Vida del ViewModel

```
Activity.onCreate()
         ↓
ViewModel creado (primera vez) o recuperado (rotación)
         ↓
ViewModel mantiene datos en memoria
         ↓
Activity.onDestroy()
         ↓
ViewModel.onCleared() → limpia recursos
         ↓
ViewModel destruido
```

**Ventaja**: Al rotar el dispositivo, los datos se mantienen sin necesidad de recargar.

---

## Dependencias

```gradle
dependencies {
    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0"
    implementation "androidx.activity:activity-ktx:1.7.0"
    
    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0"
    
    // LiveData
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.0"
}
```

---

## Estructura Recomendada

```
app/src/main/java/com/example/
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

## Tips Importantes

- ✅ ViewModel NO debe referenciar Activity/Context directamente
- ✅ Usar `viewModelScope.launch` para corrutinas (se cancela automáticamente)
- ✅ Pasar datos públicos como `LiveData` (no mutable desde fuera)
- ✅ Inyectar Repository en ViewModel (facilita testing)
- ✅ Observar LiveData en Activity (automático con ciclo de vida)

---

## Leer Más

Para más detalles, consulta el [README principal](../README.md#mvvm-architecture)

