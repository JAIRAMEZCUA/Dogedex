# 📚 Manual: RecyclerView

## Introducción

RecyclerView es un widget que muestra listas grandes de datos de forma eficiente, **reutilizando vistas** en lugar de crearlas nuevas.

### ¿Por qué "Recicler"?
- Tienes **100 perros** en la lista
- RecyclerView solo crea **5-6 vistas** (las visibles)
- Cuando scrolleas, la vista que sale se **reutiliza** para el nuevo dato
- **Resultado:** Memoria constante, app rápida ⚡

---

## Los 5 Métodos Principales

### 1️⃣ onCreateViewHolder()
```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
    val binding: ItemDogBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_dog,
        parent,
        false
    )
    return DogViewHolder(binding)
}
```

| Aspecto | Descripción |
|---------|-------------|
| **Qué hace** | Infla (convierte) XML a vista |
| **Cuándo** | 5-6 veces (solo vistas visibles) |
| **Frecuencia** | POCAS (es la clave de eficiencia) |

---

### 2️⃣ onBindViewHolder()
```kotlin
override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
    holder.bind(dogs[position])
}
```

| Aspecto | Descripción |
|---------|-------------|
| **Qué hace** | Actualiza ViewHolder con datos |
| **Cuándo** | Muchas veces (cada scroll) |
| **Frecuencia** | MUCHO pero EFICIENTE |

---

### 3️⃣ getItemCount()
```kotlin
override fun getItemCount() = dogs.size
```

| Aspecto | Descripción |
|---------|-------------|
| **Qué hace** | Retorna total de items |
| **Cuándo** | Pocas veces (cálculos) |
| **Frecuencia** | POCO |

---

### 4️⃣ submitList()
```kotlin
fun submitList(newDogs: List<Dog>) {
    dogs = newDogs
    notifyDataSetChanged()
}
```

| Aspecto | Descripción |
|---------|-------------|
| **Qué hace** | Actualiza datos y notifica cambio |
| **Cuándo** | Cuando llegan datos del ViewModel |
| **Uso** | Desde Activity al observar datos |

---

### 5️⃣ bind() (en ViewHolder)
```kotlin
fun bind(dog: Dog) {
    binding.apply {
        dogModel = dog
        root.setOnClickListener { onItemClick(dog) }
        executePendingBindings()
    }
}
```

| Aspecto | Descripción |
|---------|-------------|
| **Qué hace** | Vincula datos con vistas XML |
| **Cuándo** | Dentro de onBindViewHolder() |
| **Propósito** | Data Binding automático |

---

## Implementación Paso a Paso

### Paso 1: Crear el Modelo
```kotlin
data class Dog(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val weight: String
)
```

### Paso 2: Crear el Adapter
```kotlin
class DogAdapter(
    private val onItemClick: (Dog) -> Unit
) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {
    
    private var dogs: List<Dog> = emptyList()
    
    inner class DogViewHolder(private val binding: ItemDogBinding) 
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(dog: Dog) {
            binding.dogModel = dog
            binding.root.setOnClickListener { onItemClick(dog) }
            binding.executePendingBindings()
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_dog, parent, false
        )
        return DogViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(dogs[position])
    }
    
    override fun getItemCount() = dogs.size
    
    fun submitList(newDogs: List<Dog>) {
        dogs = newDogs
        notifyDataSetChanged()
    }
}
```

### Paso 3: Layout del Item
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="dogModel" type="com.example.model.Dog" />
    </data>
    
    <CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">
            
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="@{dogModel.name}" />
            
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="@{dogModel.weight}" />
        </LinearLayout>
    </CardView>
</layout>
```

### Paso 4: Usar en Activity
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
        binding.dogsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = dogAdapter
        }
    }
    
    private fun observeViewModel() {
        viewModel.dogs.observe(this) { dogs ->
            dogAdapter.submitList(dogs)
        }
    }
}
```

---

## Diagrama del Flujo

```
Activity.onCreate()
         ↓
setupRecyclerView() → adapter = DogAdapter()
         ↓
viewModel.loadDogs()
         ↓
Datos llegan → submitList(dogs)
         ↓
notifyDataSetChanged()
         ↓
getItemCount() → 100
         ↓
Para cada vista visible:
  ├─ onCreateViewHolder() [5-6 veces]
  └─ onBindViewHolder() → bind()
         ↓
RecyclerView renderizado ✅
         ↓
Usuario scrollea ↓
         ↓
ViewHolder reutilizado ♻️
         ↓
onBindViewHolder() con nuevos datos
         ↓
RecyclerView actualizado ✅
```

---

## Concepto Clave: El Reciclado

```
INICIO:
- 6 ViewHolders creados (solo vistas visibles)
- 5-6 datos mostrados

SCROLL:
- ViewHolder superior sale de pantalla
- Se detecta como "disponible"
- Se reutiliza para nuevo dato ♻️
- onBindViewHolder() se llama con nuevos datos
- ViewHolder renderizado en bottom

RESULTADO:
✅ Memoria: CONSTANTE (siempre 6 vistas)
✅ Performance: EXCELENTE ⚡
✅ App: RÁPIDA incluso con 10,000 items
```

---

## Tips Importantes

- ✅ Data Binding automatiza vincular datos con vistas
- ✅ `executePendingBindings()` aplica cambios inmediatamente
- ✅ `notifyDataSetChanged()` actualiza todo
- ✅ `notifyItemInserted()` más eficiente si agregar 1 item
- ✅ RecyclerView crea POCAS vistas y las REUTILIZA

---

## Leer Más

Para más detalles, consulta el [README principal](../README.md#recyclerview)

