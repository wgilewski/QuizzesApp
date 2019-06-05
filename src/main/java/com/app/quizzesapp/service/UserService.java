package com.app.quizzesapp.service;

import com.app.quizzesapp.exceptions.MyException;
import com.app.quizzesapp.model.User;
import com.app.quizzesapp.model.dto.UserDto;
import com.app.quizzesapp.model.dto.mappers.UserMapper;
import com.app.quizzesapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    public User registerUser(UserDto userDto)
    {
        if (userDto == null)
        {
            throw new MyException("USER IS NULL");
        }
        return userRepository.save(userMapper.userDtoToUser(userDto));
    }

    public List<UserDto> findAll()
    {
        return userRepository.findAll().stream().map(user ->  userMapper.userToUserDto(user)).collect(Collectors.toList());
    }

    public Optional<UserDto> findOne(Long userId)
    {
        if (userId == null)
        {
            throw new IllegalArgumentException("ID IS NULL");
        }
        return Optional.of(userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("NO USER WITH ID : " + userId)))
                .map(user -> userMapper.userToUserDto(user));
    }

}
