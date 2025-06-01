package project.domain.image.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUploadResponse {
    private String url;

    public ImageUploadResponse(String url) {
        this.url = url;
    }
}
