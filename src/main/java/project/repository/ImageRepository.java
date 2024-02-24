package project.repository;

import project.domain.Image;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>{
    
    public Optional<Image> findByUrl(String url);
}