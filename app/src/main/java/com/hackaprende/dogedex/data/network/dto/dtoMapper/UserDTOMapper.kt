package com.hackaprende.dogedex.data.network.dto.dtoMapper

import com.hackaprende.dogedex.data.network.api.models.User
import com.hackaprende.dogedex.data.network.dto.UserDTO


object UserDTOMapper {
    fun fromUserDTOToUserDomain(userDTO: UserDTO): User {
        return User(
            userDTO.id, userDTO.email, userDTO.authenticationToken
        )
    }
}