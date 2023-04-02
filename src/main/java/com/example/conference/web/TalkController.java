package com.example.conference.web;

import com.example.conference.dto.TalkDTO;
import com.example.conference.entity.Talk;
import com.example.conference.facade.TalkFacade;
import com.example.conference.payload.response.MessageResponse;
import com.example.conference.services.TalkService;
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
@RequestMapping("api/talk")
@CrossOrigin
public class TalkController {

    @Autowired
    private TalkFacade talkFacade;

    @Autowired
    private TalkService talkService;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    public ResponseEntity<Object> createTalk(@Valid @RequestBody TalkDTO talkDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Talk talk = talkService.createTalk(talkDTO, principal);
        TalkDTO createdTalk = talkFacade.talkToTalkDTO(talk);

        return new ResponseEntity<>(createdTalk, HttpStatus.OK);
    }

    @PostMapping("/{talkId}/update")
    public ResponseEntity<Object> updateTalk(@PathVariable("talkId") String talkId,
                                             @Valid @RequestBody TalkDTO talkDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Talk talk = talkService.updateTalk(talkId, talkDTO, principal);
        TalkDTO updatedTalk = talkFacade.talkToTalkDTO(talk);

        return new ResponseEntity<>(updatedTalk, HttpStatus.OK);
    }

    @PostMapping("/{talkId}/delete")
    public ResponseEntity<MessageResponse> deleteTalk(@PathVariable("talkId") String talkId,
                                                      Principal principal) {
        talkService.deleteTalk(Long.parseLong(talkId), principal);
        return new ResponseEntity<>(/*new MessageResponse("Talk was deleted"), */HttpStatus.OK);
    }

    @GetMapping("/user/talks")
    public ResponseEntity<List<TalkDTO>> getAllTalksForUser(Principal principal) {
        List<TalkDTO> talkDTOList = talkService.getAllTalksForUser(principal)
                .stream()
                .map(talkFacade::talkToTalkDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(talkDTOList, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TalkDTO>> getAllTalks() {
        List<TalkDTO> talkDTOList = talkService.getAllTalks()
                .stream()
                .map(talkFacade::talkToTalkDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(talkDTOList, HttpStatus.OK);
    }

    @GetMapping("/{title}/all")
    public ResponseEntity<List<TalkDTO>> getAllTalksForUser(@PathVariable("title") String title) {
        List<TalkDTO> talkDTOList = talkService.getAllTalksForTitle(title)
                .stream()
                .map(talkFacade::talkToTalkDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(talkDTOList, HttpStatus.OK);
    }
}
