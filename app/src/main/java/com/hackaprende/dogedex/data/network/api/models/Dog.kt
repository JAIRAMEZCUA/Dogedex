package com.hackaprende.dogedex.data.network.api.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(
    val id: Long,
    val index: Int,
    val name: String,
    val type: String,
    val heightFemale: String,
    val heightMale: String,
    val imageUrl: String,
    val lifeExpectancy: String,
    val temperament: String,
    val weightFemale: String,
    val weightMale: String,
    val inCollection: Boolean = false
) : Parcelable, Comparable<Dog> {
    //El comparable hara que comparemos lo dos objetos tipo perro, además agrega el objeto sorted
    override fun compareTo(other: Dog): Int {
        return if (this.index > other.index) {
            1
        } else {
            -1
        }
    }
}