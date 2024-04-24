package com.hoanghuy2209.profileservice.service;

import com.google.gson.Gson;
import com.hoanghuy2209.commonservice.common.CommonException;
import com.hoanghuy2209.commonservice.util.Constant;
import com.hoanghuy2209.profileservice.data.Profile;
import com.hoanghuy2209.profileservice.event.EventProducer;
import com.hoanghuy2209.profileservice.model.ProfileDTO;
import com.hoanghuy2209.profileservice.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class ProfileService {
    Gson gson = new Gson();
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private EventProducer eventProducer;

    public Flux<ProfileDTO> getAll(){
        return profileRepository.findAll().map(ProfileDTO::entityToDto).switchIfEmpty(Mono.error(new Exception("empty")));
    }

    public Mono<Boolean> checkDuplicate(String email){
        return profileRepository.existsByEmail(email);
    }

    public Mono<ProfileDTO> createProfile(ProfileDTO profileDTO){
        return checkDuplicate(profileDTO.getEmail()).flatMap(aBoolean -> {
            if(Boolean.TRUE.equals(aBoolean)){
                return Mono.error(new CommonException("PF","duplicated profile", HttpStatus.BAD_REQUEST));
            }
            profileDTO.setStatus(Constant.STATUS_PROFILE_PENDING);
            return createNewProfile(profileDTO);
        });
    }

    @Transactional
    private Mono<ProfileDTO> createNewProfile(ProfileDTO profileDTO){
        return Mono
                .just(profileDTO)
                .map(ProfileDTO::dtoToEntity)
                .flatMap(profile -> profileRepository.save(profile))
                .map(ProfileDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()))
                .doOnSuccess(profileDTO1 -> {
                    if(Objects.equals(profileDTO1.getStatus(),Constant.STATUS_PROFILE_PENDING)){
                        profileDTO1.setInitialBalance(profileDTO.getInitialBalance());
                        eventProducer.send(Constant.PROFILE_ONBOARDING_TOPIC,gson.toJson(profileDTO1)).subscribe();
                    }
                });
    }

    public Mono<ProfileDTO> updateStatusProfile(ProfileDTO profileDTO){
        return getDetailProfileByEmail(profileDTO.getEmail())
                .map(ProfileDTO::dtoToEntity)
                .flatMap(profile -> {
                    profile.setStatus(profileDTO.getStatus());
                    return profileRepository.save(profile);
                })
                .map(ProfileDTO::entityToDto)
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
    public Mono<ProfileDTO> getDetailProfileByEmail(String email){
        return profileRepository.findByEmail(email)
                .map(ProfileDTO::entityToDto)
                .switchIfEmpty(Mono.error(new CommonException("PF03", "Profile not found", HttpStatus.NOT_FOUND)));
    }
}
