package com.hoanghuy2209.profileservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.hoanghuy2209.commonservice.util.CommonFunction;
import com.hoanghuy2209.profileservice.model.ProfileDTO;
import com.hoanghuy2209.profileservice.service.ProfileService;
import com.hoanghuy2209.profileservice.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@RestController
@RequestMapping("api/v1/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    Gson gson = new Gson();

    @GetMapping()
    public ResponseEntity<Flux<ProfileDTO>> getAllProfile(){
        return ResponseEntity.ok().body(profileService.getAll());
    }

    @GetMapping("check-duplicate/{email}")
    public ResponseEntity<Mono<Boolean>> checkDuplicate(@PathVariable String email){
        return ResponseEntity.ok().body(profileService.checkDuplicate(email));
    }

    @PostMapping()
    public ResponseEntity<Mono<ProfileDTO>> createProfile(@RequestBody String requestStr) throws JsonProcessingException {
        InputStream inputStream = ProfileController.class.getClassLoader().getResourceAsStream(Constant.JSON_REQ_CREATE_PROFILE);
        CommonFunction.jsonValidate(inputStream,requestStr);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(gson.fromJson(requestStr, ProfileDTO.class)));
    }
}
