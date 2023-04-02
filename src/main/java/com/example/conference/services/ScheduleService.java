package com.example.conference.services;

import com.example.conference.dto.ScheduleDTO;
import com.example.conference.entity.Room;
import com.example.conference.entity.Schedule;
import com.example.conference.entity.Talk;
import com.example.conference.entity.User;
import com.example.conference.entity.enums.ERole;
import com.example.conference.exceptions.RoomNotFoundException;
import com.example.conference.repository.RoomRepository;
import com.example.conference.repository.ScheduleRepository;
import com.example.conference.repository.TalkRepository;
import com.example.conference.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    public static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final TalkRepository talkRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository,
                           RoomRepository roomRepository,
                           UserRepository userRepository,
                           TalkRepository talkRepository) {
        this.scheduleRepository = scheduleRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.talkRepository = talkRepository;
    }

    public Schedule createSchedule(ScheduleDTO scheduleDTO, Principal principal) {

        boolean accessError = !getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_SPEAKER);

        List<Schedule> schedules = scheduleRepository.findAllByRoom_Id(scheduleDTO.getRoomId());
        List<LocalDateTime> timeStartList = schedules.stream().map(Schedule::getTime_start).collect(Collectors.toList());
        List<LocalDateTime> timeEndList = schedules.stream().map(Schedule::getTime_end).collect(Collectors.toList());

        boolean cross = false;

        for (int i = 0; i < timeStartList.size(); i++) {
            if (scheduleDTO.getTime_start().isAfter(timeStartList.get(i)) && (scheduleDTO.getTime_start().isBefore(timeEndList.get(i)))
                    || (scheduleDTO.getTime_end().isAfter(timeStartList.get(i)) && (scheduleDTO.getTime_end().isBefore(timeEndList.get(i))))) {
                cross = true;
            }

            if (timeStartList.get(i).isAfter(scheduleDTO.getTime_start()) && (timeStartList.get(i).isBefore(scheduleDTO.getTime_end()))
                    || (timeEndList.get(i).isAfter(scheduleDTO.getTime_start()) && (timeEndList.get(i).isBefore(scheduleDTO.getTime_end())))) {
                cross = true;
            }
        }

        if (cross) {
            throw new java.lang.RuntimeException("An intersection of time was found");
        } else if (accessError) {
            throw new java.lang.RuntimeException("Access rights error");
        } else {

            User user = getUserByPrincipal(principal);
            Room room = roomRepository.findById(scheduleDTO.getRoomId())
                    .orElseThrow(() -> new RoomNotFoundException("Room cannot be found for username: " + user.getEmail()));
            Talk talk = talkRepository.findById(scheduleDTO.getTalkId())
                    .orElseThrow(() -> new RoomNotFoundException("Talk cannot be found for username: " + user.getEmail()));

            Schedule schedule = new Schedule();
            schedule.setRoom(room);
            schedule.setTalk(talk);
            schedule.setTime_start(scheduleDTO.getTime_start());
            schedule.setTime_end(scheduleDTO.getTime_end());

            LOG.info("Saving schedule for Room: {}", room.getId());
            return scheduleRepository.save(schedule);
        }
    }

    public List<Schedule> getAllSchedulesForRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room cannot be found"));
        return scheduleRepository.findAllByRoom_Id(room.getId());
    }

    public void deleteSchedule(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        schedule.ifPresent(scheduleRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}