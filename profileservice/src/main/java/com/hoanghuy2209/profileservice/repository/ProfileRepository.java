package com.hoanghuy2209.profileservice.repository;

import com.hoanghuy2209.profileservice.data.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfileRepository extends ReactiveCrudRepository<Profile,Long> {
    Mono<Boolean> existsByEmail(String email);

    Mono<Profile> findByEmail(String email);
}
