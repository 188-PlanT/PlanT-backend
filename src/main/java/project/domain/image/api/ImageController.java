package project.domain.image.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.domain.image.dto.response.ImageUploadResponse;
import project.domain.image.service.ImageService;
import project.infra.s3.application.S3Service;

@Tag(name = "5. [Image]", description = "이미지 업로드 API")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;
    private final ImageService imageService;

    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다. 이미지 URL을 반환합니다.")
    @PostMapping("/v1/image")
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("image") MultipartFile image) throws IOException {
        String url = s3Service.uploadFile(image);

        imageService.save(url);
        ImageUploadResponse response = new ImageUploadResponse(url);
        return ResponseEntity.ok(response);
    }
}
