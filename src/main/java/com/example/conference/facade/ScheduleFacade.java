package com.example.conference.facade;

import com.example.conference.dto.ScheduleDTO;
import com.example.conference.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFacade {
    public ScheduleDTO scheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setRoomId(schedule.getRoom().getId());
        scheduleDTO.setRoomName(schedule.getRoom().getName());
        scheduleDTO.setTalkId(schedule.getTalk().getId());
        scheduleDTO.setTalkTitle(schedule.getTalk().getTitle());
        scheduleDTO.setTime_start(schedule.getTime_start());
        scheduleDTO.setTime_end(schedule.getTime_end());
        return scheduleDTO;
    }
}