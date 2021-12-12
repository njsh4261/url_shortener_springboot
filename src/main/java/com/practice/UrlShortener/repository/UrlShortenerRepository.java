package com.practice.UrlShortener.repository;

import com.practice.UrlShortener.model.UrlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlData, Long> {
    UrlData save(UrlData urldata);
    Optional<UrlData> findById(Long id);
    Optional<UrlData> findByUrl(String url);
}
