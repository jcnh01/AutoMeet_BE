package com.AutoMeet.domain.meetingRoom.repository;

import com.AutoMeet.domain.meetingRoom.model.MeetingRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MeetingRoomRepository extends MongoRepository<MeetingRoom, String> {
    Optional<MeetingRoom> findByMeetingId(String meetingId);
}
