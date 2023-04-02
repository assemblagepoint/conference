package com.example.conference.facade;

import com.example.conference.dto.RoomDTO;
import com.example.conference.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomFacade {
    public RoomDTO roomToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        return roomDTO;
    }
}