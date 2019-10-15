package com.liseh.auth.service.impl;

import com.liseh.GenericKafkaObject;
import com.liseh.auth.constant.UserEvent;
import com.liseh.auth.persistence.dto.UserDto;
import com.liseh.auth.persistence.entity.User;
import com.liseh.auth.repository.UserRepository;
import com.liseh.auth.service.UserEventService;
import com.liseh.auth.service.UserRegistrationEventService;
import com.liseh.auth.util.CommonUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEventServiceImpl implements UserEventService, UserRegistrationEventService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserEventServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onUserEvent(GenericKafkaObject object) {
        switch (object.getEventName()) {
            case UserEvent.USER_REGISTRATION:
                onUserRegistration(object);
                break;
             default:
                 break;
        }
    }

    @Override
    public void onUserRegistration(GenericKafkaObject object) {
        UserDto userDto = CommonUtil.getObject(object.getContent(), UserDto.class);
        if (userDto == null) {
            return;
        }
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }
}
