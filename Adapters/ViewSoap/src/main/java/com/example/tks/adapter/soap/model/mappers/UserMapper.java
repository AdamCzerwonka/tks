package com.example.tks.adapter.soap.model.mappers;

import com.example.tks.adapter.soap.model.dto.User.UserResponse;
import com.example.tks.core.domain.model.User;

public class UserMapper {
    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getActive(), user.getRole());
    }
}
