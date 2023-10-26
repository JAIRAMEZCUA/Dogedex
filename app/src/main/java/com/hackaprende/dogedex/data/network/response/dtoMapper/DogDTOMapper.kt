package com.hackaprende.dogedex.data.network.response.dtoMapper

import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.response.dto.DogDTO


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