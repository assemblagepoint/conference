package com.example.conference.web;

import com.example.conference.dto.UserDTO;
import com.example.conference.entity.User;
import com.example.conference.facade.UserFacade;
import com.example.conference.payload.request.SignupRequest;
import com.example.conference.payload.response.MessageResponse;
import com.example.conference.services.UserService;
import com.example.conference.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;


        User user = userService.updateUser(userDTO, principal);

        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody SignupRequest signupRequest,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUserByAdmin(signupRequest, principal);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/{userId}/update")
    public ResponseEntity<Object> updateUser(@PathVariable("userId") String userId,
                                             @Valid @RequestBody UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUserById(userId, userDTO, principal);

        UserDTO userUpdated = userFacade.userToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    @GetMapping("/{userId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") String userId,
                                             Principal principal) {
        userService.deleteUserById(Long.parseLong(userId), principal);
        return new ResponseEntity<>(/*new MessageResponse("User was deleted"),*/ HttpStatus.OK);
    }

    @GetMapping("/{userId}/setrolespeaker")
    public ResponseEntity<Object> setRoleSpeakerUser(@PathVariable("userId") String userId,
                                                     Principal principal) {

        userService.setRoleSpeakerById(Long.parseLong(userId), principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}