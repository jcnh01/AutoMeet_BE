package com.AutoMeet.domain.meet.repository;

import com.AutoMeet.domain.meet.model.Meet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetRepository extends MongoRepository<Meet, String> {
}
