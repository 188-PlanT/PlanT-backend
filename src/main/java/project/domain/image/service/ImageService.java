package project.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.common.constant.UrlConstant;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.domain.image.dao.ImageRepository;
import project.domain.image.domain.Image;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void save(String imageUrl) {
        Image image = new Image(imageUrl);
        imageRepository.save(image);
    }

    public Image getDefaultUserProfile() {
        return imageRepository
                .findByUrl(UrlConstant.DEFAULT_USER_PROFILE_URL)
                .orElseThrow(() -> new PlantException(ErrorCode.IMAGE_NOT_FOUND));
    }
}
