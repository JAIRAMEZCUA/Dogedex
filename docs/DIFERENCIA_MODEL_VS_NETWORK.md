# 📚 Diferencia: model/ vs network/

## Explicación Sencilla

### 📁 **network/** (DTOs - Data Transfer Objects)
**¿Qué es?** Modelos que representan los datos que vienen de la **API**

```kotlin
// data/network/DogNetworkModel.kt
@Serializable
data class DogNetworkModel(
    val id: Int,
    val name: String,
    val image: String,
    val breed: String,
    val weight: String
)
```

**Características:**
- ✅ Coinciden exactamente con la respuesta JSON de la API
- ✅ Tienen anotaciones de serialización (@Serializable, @SerializedName)
- ✅ Solo se usan para recibir datos de la API
- ✅ Pueden tener campos que no necesitas en tu app

---

### 📁 **model/** (Entidades - Entities)
**¿Qué es?** Modelos que representan los datos para la **Base de Datos Local (Room)**

```kotlin
// data/model/DogEntity.kt
@Entity(tableName = "dogs")
data class DogEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val breed: String
)
```

**Características:**
- ✅ Coinciden con las columnas de la tabla en Room
- ✅ Tienen anotaciones de Room (@Entity, @PrimaryKey)
- ✅ Solo se usan para guardar/leer de la BD
- ✅ Solo tienen los campos que necesitas

---

## 🔄 Diferencia Visual

```
API (JSON)
    ↓
network/DogNetworkModel  ← Recibe datos de la API
    ↓
(Conversión toDomain() o toEntity())
    ↓
domain/Dog  ← Modelo de dominio (lógica de negocio)
    ↓
(Conversión)
    ↓
model/DogEntity  ← Guarda en la BD local
    ↓
Room (Base de Datos)
```

---

## 📊 Tabla Comparativa

| Aspecto | **network/** | **model/** |
|---------|---|---|
| **Propósito** | Recibir datos de API | Guardar en BD local |
| **Ubicación** | `data/network/` | `data/model/` |
| **Anotaciones** | @Serializable, @SerializedName | @Entity, @PrimaryKey |
| **Origen** | API / JSON | Room / Base de datos |
| **Uso** | Retrofit, HttpClient | Room DAO |
| **Ejemplo** | DogNetworkModel | DogEntity |

---

## 💡 Ejemplo Real

### Respuesta de la API (JSON)
```json
{
  "id": 1,
  "name": "Bulldog",
  "image": "https://...",
  "breed": "Bulldog Inglés",
  "weight": "40kg",
  "age": 5,
  "origin": "England"
}
```

### DogNetworkModel (Recibe TODO de la API)
```kotlin
@Serializable
data class DogNetworkModel(
    val id: Int,
    val name: String,
    val image: String,
    val breed: String,
    val weight: String,
    val age: Int,
    val origin: String  // Puede que no lo necesites
)
```

### DogEntity (Guarda SOLO lo necesario en BD)
```kotlin
@Entity
data class DogEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val breed: String,
    val weight: String
    // age y origin no se guardan
)
```

---

## 🔁 Conversión Entre Modelos

```kotlin
// Convertir NetworkModel a Entity
fun DogNetworkModel.toEntity(): DogEntity {
    return DogEntity(
        id = id,
        name = name,
        image = image,
        breed = breed,
        weight = weight
        // No incluir: age, origin
    )
}

// Convertir Entity a Domain
fun DogEntity.toDomain(): Dog {
    return Dog(
        id = id,
        name = name,
        image = image,
        breed = breed,
        weight = weight
    )
}
```

---

## ✨ Resumen Sencillo

```
network/DogNetworkModel
    └─ Recibe TODO de la API
    
    ↓ (conversión)
    
model/DogEntity
    └─ Guarda SOLO lo necesario en BD
```

---

## 🎯 Ejemplo Paso a Paso

### 1. API devuelve JSON
```json
{
  "id": 1,
  "name": "Bulldog",
  "image": "...",
  "breed": "...",
  "weight": "...",
  "age": 5
}
```

### 2. Retrofit lo convierte a DogNetworkModel
```kotlin
val networkModel: DogNetworkModel = apiService.getDogs()[0]
// Tiene: id, name, image, breed, weight, age
```

### 3. Convertimos a Entity
```kotlin
val entity: DogEntity = networkModel.toEntity()
// Tiene: id, name, image, breed, weight (SIN age)
```

### 4. Room guarda en BD
```kotlin
dogDao.insert(entity)
// Guardado en tabla dogs
```

---

## ❓ ¿Por Qué Dos Modelos?

**Razón 1:** La API puede devolver más datos de los que necesitas
- La API devuelve 10 campos
- Tú solo necesitas 5
- No gastas espacio en BD guardando los 5 innecesarios

**Razón 2:** La estructura puede ser diferente
- API: `{user: {name: "..."}}`
- BD: `name: "..."`
- Los modelos acomodan estas diferencias

**Razón 3:** Cada capa tiene su necesidad
- API necesita @Serializable
- BD necesita @Entity
- No pueden ser los mismos

---

## 📌 Conclusión

```
network/ = Lo que viene de AFUERA (API)
model/   = Lo que guardas ADENTRO (BD local)
```

**Son diferentes porque:**
- ✅ Diferentes anotaciones
- ✅ Diferentes propósitos
- ✅ Pueden tener diferentes campos
- ✅ Una recibe datos, otra los guarda

