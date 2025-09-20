package project.domain.image.dto.response;

public record ImageUploadResponse(String url) {

    public static ImageUploadResponse of(String url) {
        return new ImageUploadResponse(url);
    }
}
