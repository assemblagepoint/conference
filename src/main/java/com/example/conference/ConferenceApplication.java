package com.example.conference;

import com.example.conference.entity.Room;
import com.example.conference.entity.Schedule;
import com.example.conference.entity.Talk;
import com.example.conference.entity.User;
import com.example.conference.entity.enums.ERole;
import com.example.conference.repository.RoomRepository;
import com.example.conference.repository.ScheduleRepository;
import com.example.conference.repository.TalkRepository;
import com.example.conference.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ConferenceApplication {

    private final BCryptPasswordEncoder passwordEncoder;

    public ConferenceApplication(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConferenceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository,
                                      TalkRepository talkRepository,
                                      RoomRepository roomRepository,
                                      ScheduleRepository scheduleRepository) {
        return (args) -> {
            Set<ERole> rolesforadmin = new HashSet<>();
            rolesforadmin.add(ERole.ROLE_ADMIN);
            rolesforadmin.add(ERole.ROLE_LISTENER);

            Set<ERole> roles = new HashSet<>();
            roles.add(ERole.ROLE_LISTENER);

            User Admin = new User("admin", "admin", "admin", "admin@mail.io", passwordEncoder.encode("adminpassword"), rolesforadmin);

            User Userivan = new User("ivan", "ivanivanov", "ivanov", "ivanivanov@mail.io", passwordEncoder.encode("ivanpassword"), roles);
            User Useralex = new User("alex", "alex", "alex", "alex@mail.io", passwordEncoder.encode("alexpassword"), roles);
            User Userjohn = new User("john", "johnsmith", "smith", "johnsmith@mail.io", passwordEncoder.encode("johnpassword"), roles);

            userRepository.save(Admin);
            userRepository.save(Userivan);
            userRepository.save(Useralex);
            userRepository.save(Userjohn);


            Set<User> usersfortalk1 = new HashSet<>();
            usersfortalk1.add(Userivan);
            usersfortalk1.add(Useralex);
            Set<User> usersfortalk2 = new HashSet<>();
            usersfortalk2.add(Useralex);
            Set<User> usersfortalk4 = new HashSet<>();
            usersfortalk4.add(Userjohn);

            Talk talk1 = new Talk("Название доклада", 60, usersfortalk1);
            Talk talk3 = new Talk("Ещё один доклад", 60, usersfortalk1);
            Talk talk2 = new Talk("Title of the talk", 90, usersfortalk2);
            Talk talk4 = new Talk("Another talk", 90, usersfortalk4);

            talkRepository.save(talk1);
            talkRepository.save(talk2);
            talkRepository.save(talk3);
            talkRepository.save(talk4);


            Room room1 = new Room("Аудитория №1");
            Room room2 = new Room("Аудитория №2");
            Room room3 = new Room("Аудитория №3");

            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);


            Schedule schedulefortalk1 = new Schedule(room1, talk1,
                    LocalDateTime.of(2021, 9, 10, 14, 0),
                    LocalDateTime.of(2021, 9, 10, 15, 0));

            Schedule schedulefortalk2 = new Schedule(room2, talk2,
                    LocalDateTime.of(2021, 9, 10, 10, 0),
                    LocalDateTime.of(2021, 9, 10, 11, 30));

            Schedule schedulefortalk3 = new Schedule(room3, talk3,
                    LocalDateTime.of(2021, 9, 10, 12, 0),
                    LocalDateTime.of(2021, 9, 10, 13, 0));

            Schedule schedulefortalk4 = new Schedule(room2, talk4,
                    LocalDateTime.of(2021, 9, 10, 12, 0),
                    LocalDateTime.of(2021, 9, 10, 13, 30));

            scheduleRepository.save(schedulefortalk1);
            scheduleRepository.save(schedulefortalk2);
            scheduleRepository.save(schedulefortalk3);
            scheduleRepository.save(schedulefortalk4);
        };
    }
}