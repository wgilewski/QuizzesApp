package com.app.quizzesapp.model.dto.mappers;

import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper
{
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
}
