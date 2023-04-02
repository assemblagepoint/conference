package com.example.conference.web;

import com.example.conference.dto.RoomDTO;
import com.example.conference.facade.RoomFacade;
import com.example.conference.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/room")
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomFacade roomFacade;

    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> roomDTOList = roomService.getAllRooms()
                .stream()
                .map(roomFacade::roomToRoomDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }
}