package com.example.conference.repository;

import com.example.conference.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

    List<Talk> findAllByUsers_Id(Long id);

    Optional<Talk> findTalkByIdAndUsers_Id(Long id, Long i);

    Optional<Talk> findTalkByTitle(String title);
}