package se325.assignment01.concert.service.mapper;

import se325.assignment01.concert.common.dto.UserDTO;
import se325.assignment01.concert.service.domain.User;

public class UserMapper {

    static UserDTO toDto(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getUsername());
    }

    public static User toDomainModel(UserDTO dtoUser) {
        return new User(
                dtoUser.getUsername(),
                dtoUser.getPassword());
    }
}
