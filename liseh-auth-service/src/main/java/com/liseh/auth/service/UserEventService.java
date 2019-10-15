package com.liseh.auth.service;

import com.liseh.GenericKafkaObject;

public interface UserEventService {
    void onUserEvent(GenericKafkaObject object);
}
