package com.example.conference.services;

import com.example.conference.dto.TalkDTO;
import com.example.conference.entity.Talk;
import com.example.conference.entity.User;
import com.example.conference.entity.enums.ERole;
import com.example.conference.exceptions.AccessRightsException;
import com.example.conference.exceptions.TalkNotFoundException;
import com.example.conference.repository.TalkRepository;
import com.example.conference.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TalkService {

    private final TalkRepository talkRepository;
    private final UserRepository userRepository;

    @Autowired
    public TalkService(TalkRepository talkRepository, UserRepository userRepository) {
        this.talkRepository = talkRepository;
        this.userRepository = userRepository;
    }

    public Talk createTalk(TalkDTO talkDTO, Principal principal) {
        Talk talk = new Talk();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_SPEAKER)) {
            User user = getUserByPrincipal(principal);

            Set<User> users = new HashSet<>();
            users.add(user);

            talk.setUsers(users);
            talk.setTitle(talkDTO.getTitle());
            talk.setDuration(talkDTO.getDuration());
        }
        try {
            return talkRepository.save(talk);
        } catch (Exception e) {
            throw new AccessRightsException("Only the speaker can create Talks");
        }
    }

    public Talk updateTalk(String talkId, TalkDTO talkDTO, Principal principal) {
        Talk talk = getTalkById(Long.parseLong(talkId), principal);
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_SPEAKER)) {
            talk.setTitle(talkDTO.getTitle());
            talk.setDuration(talkDTO.getDuration());
        }
        try {
            return talkRepository.save(talk);
        } catch (Exception e) {
            throw new AccessRightsException("Only the speaker can update Talks");
        }
    }

    public void deleteTalk(Long talkId, Principal principal) {
        Talk talk = new Talk();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_SPEAKER)) {
            talk = getTalkById(talkId, principal);
        }
        talkRepository.delete(talk);
    }

    public List<Talk> getAllTalksForUser(Principal principal) {
        User tempuser = new User();
        if (getUserByPrincipal(principal).getRoles().contains(ERole.ROLE_SPEAKER)) {
            tempuser = getUserByPrincipal(principal);
        }
        return talkRepository.findAllByUsers_Id(tempuser.getId());
    }

    public List<Talk> getAllTalks() {
        return talkRepository.findAll();
    }

    public Talk getTalkById(Long talkId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return talkRepository.findTalkByIdAndUsers_Id(talkId, user.getId())
                .orElseThrow(() -> new TalkNotFoundException("Talk cannot be found for username: " + user.getEmail()));
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public Optional<Talk> getAllTalksForTitle(String title) {
        return talkRepository.findTalkByTitle(title);
    }
}