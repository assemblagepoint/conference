package com.example.conference.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RoomDTO {

    private Long id;

    @NotEmpty
    private String name;
}