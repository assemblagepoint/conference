package com.example.conference.web;

import com.example.conference.dto.ScheduleDTO;
import com.example.conference.entity.Schedule;
import com.example.conference.facade.ScheduleFacade;
import com.example.conference.payload.response.MessageResponse;
import com.example.conference.services.ScheduleService;
import com.example.conference.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/schedule")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleFacade scheduleFacade;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody ScheduleDTO scheduleDTO,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Schedule schedule = scheduleService.createSchedule(scheduleDTO, principal);
        ScheduleDTO createdSchedule = scheduleFacade.scheduleToScheduleDTO(schedule);

        return new ResponseEntity<>(createdSchedule, HttpStatus.OK);
    }

    @GetMapping("/{roomId}/all")
    public ResponseEntity<List<ScheduleDTO>> getAllCommentsToPost(@PathVariable("roomId") String roomId) {
        List<ScheduleDTO> scheduleDTOList = scheduleService.getAllSchedulesForRoom(Long.parseLong(roomId))
                .stream()
                .map(scheduleFacade::scheduleToScheduleDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(scheduleDTOList, HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}/delete")
    public ResponseEntity<MessageResponse> deleteSchedule(@PathVariable("scheduleId") String scheduleId) {
        scheduleService.deleteSchedule(Long.parseLong(scheduleId));
        return new ResponseEntity<>(new MessageResponse("Schedule was deleted"), HttpStatus.OK);
    }
}