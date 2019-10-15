package com.liseh.auth.service;

import com.liseh.GenericKafkaObject;

public interface UserRegistrationEventService {
    void onUserRegistration(GenericKafkaObject object);
}
