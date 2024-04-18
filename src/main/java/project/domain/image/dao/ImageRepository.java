package project.domain.image.dao;

import project.domain.image.domain.Image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{
    
    public Optional<Image> findByUrl(String url);
}