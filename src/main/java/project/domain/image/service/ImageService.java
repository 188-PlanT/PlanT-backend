package project.domain.image.service;

import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;


import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void save(String imageUrl) {
        Image image = new Image(imageUrl);
        imageRepository.save(image);
    }
}