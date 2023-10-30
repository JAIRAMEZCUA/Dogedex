package com.hackaprende.dogedex.data.network.dto.dtoMapper

import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.dto.dog.DogDTO


object DogDTOMapper {

    private fun fromDogDTOToDogDomain(dogDTO: DogDTO): Dog {
        return Dog(
            dogDTO.id, dogDTO.index, dogDTO.name, dogDTO.type,
            dogDTO.heightFemale, dogDTO.heightMale, dogDTO.imageUrl, dogDTO.lifeExpectancy,
            dogDTO.temperament, dogDTO.weightFemale, dogDTO.weightMale
        )
    }

    fun fromDogDTOListToDogDomainList(dogDTOList: List<DogDTO>): List<Dog> {
        return dogDTOList.map { fromDogDTOToDogDomain(it) }
    }

}