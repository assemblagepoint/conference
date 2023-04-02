package com.example.conference.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ScheduleDTO {

    private Long id;

    private Long roomId;

    private String roomName;

    @NotNull
    private Long talkId;

    private String talkTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime time_start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime time_end;
}