# 📚 Manual: Arquitectura MVVM + Clean Architecture

## Introducción

La arquitectura **MVVM + Clean Architecture** es un patrón que separa el código en capas independientes:

- **Presentation Layer** (MVVM) - UI y lógica de presentación
- **Domain Layer** - Casos de uso y lógicas de negocio
- **Data Layer** - Acceso a datos (API, BD)
- **DI Layer** - Inyección de dependencias

---

## Estructura de Carpetas

```
com.hackaprende.dogedex/
│
├── 📁 presentation/          (CAPA DE PRESENTACIÓN - MVVM)
│   ├── view/
│   │   ├── activity/         (Activities con UI)
│   │   └── fragment/         (Fragments con UI)
│   └── viewmodel/            (ViewModels con lógica de presentación)
│
├── 📁 domain/                (CAPA DE DOMINIO)
│   ├── repository/           (Interfaces de repositorios)
│   └── usecase/              (Casos de uso / Interactors)
│
├── 📁 data/                  (CAPA DE DATOS)
│   ├── datasource/
│   │   ├── local/            (Base de datos local - Room)
│   │   └── remote/           (API remota - Retrofit)
│   ├── repository/           (Implementación de repositorios)
│   ├── model/                (Modelos de datos locales)
│   └── network/              (Modelos de API - DTOs)
│
└── 📁 di/                    (INYECCIÓN DE DEPENDENCIAS)
    └── (Módulos Hilt o Dagger)
```

---

## 🏗️ Las 4 Capas Explicadas

### 1️⃣ PRESENTATION LAYER (MVVM)

**Ubicación**: `presentation/`

**Responsabilidad**: Mostrar UI y manejar interacción del usuario

**Componentes**:

#### **Activity** → `presentation/view/activity/`
```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    // Únicamente UI
    // Observa ViewModel
    // Maneja ciclo de vida
}
```

#### **Fragment** → `presentation/view/fragment/`
```kotlin
// DogListFragment.kt
class DogListFragment : Fragment() {
    // Únicamente UI
    // Observa ViewModel
}
```

#### **ViewModel** → `presentation/viewmodel/`
```kotlin
// DogListViewModel.kt
class DogListViewModel(
    val getDogListUseCase: GetDogListUseCase
) : ViewModel() {
    // Lógica de presentación
    // Observar datos
    // NO accede a BD/API directamente
}
```

---

### 2️⃣ DOMAIN LAYER

**Ubicación**: `domain/`

**Responsabilidad**: Lógica de negocio (casos de uso)

**Componentes**:

#### **Repository Interface** → `domain/repository/`
```kotlin
// DogRepository.kt (INTERFAZ)
interface DogRepository {
    suspend fun getDogs(): Result<List<Dog>>
    suspend fun getDogById(id: Int): Result<Dog>
}
```

#### **UseCase** → `domain/usecase/`
```kotlin
// GetDogListUseCase.kt
class GetDogListUseCase(
    private val repository: DogRepository
) {
    suspend operator fun invoke(): Result<List<Dog>> {
        // Lógica de negocio
        return repository.getDogs()
    }
}
```

---

### 3️⃣ DATA LAYER

**Ubicación**: `data/`

**Responsabilidad**: Acceso a datos (BD, API)

**Componentes**:

#### **Remote DataSource** → `data/datasource/remote/`
```kotlin
// DogRemoteDataSource.kt
class DogRemoteDataSource(
    private val apiService: DogApiService
) {
    suspend fun getDogs(): List<DogNetworkModel> {
        return apiService.getDogs()
    }
}
```

#### **Local DataSource** → `data/datasource/local/`
```kotlin
// DogLocalDataSource.kt
class DogLocalDataSource(
    private val dogDao: DogDao
) {
    fun getDogs(): List<DogEntity> {
        return dogDao.getAll()
    }
}
```

#### **Repository Implementation** → `data/repository/`
```kotlin
// DogRepositoryImpl.kt
class DogRepositoryImpl(
    private val remote: DogRemoteDataSource,
    private val local: DogLocalDataSource
) : DogRepository {
    override suspend fun getDogs(): Result<List<Dog>> {
        return try {
            val dogs = remote.getDogs()
            Result.success(dogs.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### **Network Models** → `data/network/`
```kotlin
// DogNetworkModel.kt (DTO - Data Transfer Object)
@Serializable
data class DogNetworkModel(
    val id: Int,
    val name: String,
    val image: String
)
```

#### **Local Models** → `data/model/`
```kotlin
// DogEntity.kt (Para Room)
@Entity
data class DogEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String
)
```

---

### 4️⃣ DI LAYER (Inyección de Dependencias)

**Ubicación**: `di/`

**Responsabilidad**: Crear y proveer instancias de clases

**Componentes**:

#### **Módulo Hilt** → `di/DogModule.kt`
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DogModule {
    
    @Provides
    @Singleton
    fun provideApiService(): DogApiService {
        return DogApiService() // O Retrofit, etc
    }
    
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: DogApiService
    ): DogRemoteDataSource {
        return DogRemoteDataSource(apiService)
    }
    
    @Provides
    @Singleton
    fun provideDogRepository(
        remote: DogRemoteDataSource,
        local: DogLocalDataSource
    ): DogRepository {
        return DogRepositoryImpl(remote, local)
    }
    
    @Provides
    fun provideGetDogListUseCase(
        repository: DogRepository
    ): GetDogListUseCase {
        return GetDogListUseCase(repository)
    }
}
```

---

## 🔄 Flujo de Datos Completo

```
1. Activity/Fragment (UI)
   ↓
2. ViewModel (observa datos)
   ↓
3. UseCase (lógica de negocio)
   ↓
4. Repository Interface (contrato)
   ↓
5. Repository Implementation (implementación)
   ↓
6. DataSource (remote/local)
   ├─ Remote → API (Retrofit)
   └─ Local → BD (Room)
   ↓
7. Modelos (NetworkModel, Entity)
   ↓
8. Volver al ViewModel con datos
   ↓
9. Activity/Fragment observa y renderiza
```

---

## 📋 Ventajas de Esta Arquitectura

| Ventaja | Explicación |
|---------|-------------|
| **Escalable** | Fácil agregar features sin afectar código existente |
| **Testeable** | Cada capa se puede testear independientemente |
| **Mantenible** | Código organizado y claro |
| **Reutilizable** | Componentes se pueden reutilizar |
| **Independencia** | Cambios en BD no afectan UI |

---

## 📝 Ejemplo: GetDogList Feature

### Paso 1: Crear el Domain Model
```kotlin
// domain/model/Dog.kt
data class Dog(
    val id: Int,
    val name: String,
    val image: String
)
```

### Paso 2: Crear Repository Interface
```kotlin
// domain/repository/DogRepository.kt
interface DogRepository {
    suspend fun getDogs(): Result<List<Dog>>
}
```

### Paso 3: Crear UseCase
```kotlin
// domain/usecase/GetDogListUseCase.kt
class GetDogListUseCase(
    private val repository: DogRepository
) {
    suspend operator fun invoke(): Result<List<Dog>> {
        return repository.getDogs()
    }
}
```

### Paso 4: Crear RemoteDataSource
```kotlin
// data/datasource/remote/DogRemoteDataSource.kt
class DogRemoteDataSource(
    private val apiService: DogApiService
) {
    suspend fun getDogs(): List<DogNetworkModel> {
        return apiService.getDogs()
    }
}
```

### Paso 5: Crear Repository Implementation
```kotlin
// data/repository/DogRepositoryImpl.kt
class DogRepositoryImpl(
    private val remote: DogRemoteDataSource
) : DogRepository {
    override suspend fun getDogs(): Result<List<Dog>> {
        return try {
            val networkDogs = remote.getDogs()
            val domainDogs = networkDogs.map { it.toDomain() }
            Result.success(domainDogs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Paso 6: Crear ViewModel
```kotlin
// presentation/viewmodel/DogListViewModel.kt
class DogListViewModel(
    private val getDogListUseCase: GetDogListUseCase
) : ViewModel() {
    
    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> = _dogs
    
    fun loadDogs() {
        viewModelScope.launch {
            val result = getDogListUseCase()
            result.onSuccess { dogs ->
                _dogs.value = dogs
            }
        }
    }
}
```

### Paso 7: Crear Activity
```kotlin
// presentation/view/activity/DogListActivity.kt
class DogListActivity : AppCompatActivity() {
    
    private val viewModel: DogListViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_list)
        
        viewModel.dogs.observe(this) { dogs ->
            // Actualizar UI
            adapter.submitList(dogs)
        }
        
        viewModel.loadDogs()
    }
}
```

### Paso 8: Configurar DI
```kotlin
// di/DogModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DogModule {
    
    @Provides
    fun provideGetDogListUseCase(
        repository: DogRepository
    ): GetDogListUseCase {
        return GetDogListUseCase(repository)
    }
    
    @Provides
    fun provideDogRepository(
        remote: DogRemoteDataSource
    ): DogRepository {
        return DogRepositoryImpl(remote)
    }
    
    @Provides
    fun provideDogRemoteDataSource(
        apiService: DogApiService
    ): DogRemoteDataSource {
        return DogRemoteDataSource(apiService)
    }
}
```

---

## 🎯 Resumen de Responsabilidades

| Capa | Responsabilidad | Ejemplo |
|------|-----------------|---------|
| **Presentation** | Mostrar UI | Activity, Fragment, ViewModel |
| **Domain** | Lógica de negocio | UseCase, Repository (interfaz) |
| **Data** | Acceso a datos | DataSource, Repository (impl) |
| **DI** | Proveer instancias | Módulos Hilt |

---

## 📚 Estructura de Directorios Completa

```
com.hackaprende.dogedex/

presentation/
├── view/
│   ├── activity/
│   │   └── MainActivity.kt
│   └── fragment/
│       └── DogListFragment.kt
└── viewmodel/
    └── DogListViewModel.kt

domain/
├── model/
│   └── Dog.kt
├── repository/
│   └── DogRepository.kt (interfaz)
└── usecase/
    └── GetDogListUseCase.kt

data/
├── datasource/
│   ├── local/
│   │   └── DogLocalDataSource.kt
│   └── remote/
│       └── DogRemoteDataSource.kt
├── model/
│   └── DogEntity.kt (Room)
├── network/
│   ├── DogNetworkModel.kt
│   └── DogApiService.kt
└── repository/
    └── DogRepositoryImpl.kt

di/
└── DogModule.kt
```

---

## ✅ Checklist para Implementar

- [ ] Crear Domain Model (`domain/model/`)
- [ ] Crear Repository Interface (`domain/repository/`)
- [ ] Crear UseCase (`domain/usecase/`)
- [ ] Crear RemoteDataSource (`data/datasource/remote/`)
- [ ] Crear LocalDataSource (`data/datasource/local/`)
- [ ] Crear Network Models (`data/network/`)
- [ ] Crear Repository Implementation (`data/repository/`)
- [ ] Crear ViewModel (`presentation/viewmodel/`)
- [ ] Crear Activity/Fragment (`presentation/view/`)
- [ ] Configurar DI (`di/`)

---

## 📞 Dependencias Necesarias

```gradle
dependencies {
    // Hilt (DI)
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-compiler:2.48'
    
    // Room (BD local)
    implementation 'androidx.room:room-runtime:2.6.0'
    kapt 'androidx.room:room-compiler:2.6.0'
    
    // Retrofit (API)
    implementation 'com.squareup.retrofit2:retrofit:2.10.0'
    
    // ViewModel y LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.0'
}
```

---

## 🎓 Conclusión

Con esta arquitectura logras:
- ✅ **Separación de responsabilidades**
- ✅ **Código testeable**
- ✅ **Fácil mantenimiento**
- ✅ **Escalabilidad**
- ✅ **Reutilización de código**

**¡Ahora tienes una base sólida para desarrollar! 🚀**

