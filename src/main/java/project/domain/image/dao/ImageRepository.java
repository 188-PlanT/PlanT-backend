package project.domain.image.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.image.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    public Optional<Image> findByUrl(String url);
}
