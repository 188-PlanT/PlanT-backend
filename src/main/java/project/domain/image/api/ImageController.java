package project.domain.image.api;

import project.domain.image.dto.ImageUploadResponse;
import project.domain.image.service.ImageService;
import project.domain.image.service.S3Service;

import lombok.RequiredArgsConstructor;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;
    private final ImageService imageService;

    @PostMapping("/v1/image")
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("image") MultipartFile image) throws IOException {
        String url = s3Service.uploadFile(image);

        imageService.save(url);
        ImageUploadResponse response = new ImageUploadResponse(url);
        return ResponseEntity.ok(response);
    }
}