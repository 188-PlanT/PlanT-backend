package project.domain.image.service;

import org.springframework.beans.factory.annotation.Value;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;


import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${s3.default-image-url.user}")
    private String DEFAULT_USER_IMAGE_URL;

    public void save(String imageUrl) {
        Image image = new Image(imageUrl);
        imageRepository.save(image);
    }

    public Image getDefaultUserProfile(){
        return imageRepository.findByUrl(DEFAULT_USER_IMAGE_URL)
                .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
    }
}