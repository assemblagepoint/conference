package com.example.conference.facade;

import com.example.conference.dto.TalkDTO;
import com.example.conference.entity.Talk;
import org.springframework.stereotype.Component;

@Component
public class TalkFacade {
    public TalkDTO talkToTalkDTO(Talk talk) {
        TalkDTO talkDTO = new TalkDTO();
        talkDTO.setId(talk.getId());
        talkDTO.setTitle(talk.getTitle());
        talkDTO.setDuration(talk.getDuration());
        return talkDTO;
    }
}