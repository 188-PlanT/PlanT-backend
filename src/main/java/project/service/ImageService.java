package project.service;

import project.repository.ImageRepository;
import project.domain.Image;
import project.dto.image.ImageDto;

import java.util.List;


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