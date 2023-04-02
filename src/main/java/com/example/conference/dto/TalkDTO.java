package com.example.conference.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TalkDTO {

    private Long id;

    @NotEmpty
    private String title;

    private Integer duration;
}