package project.domain.image.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageDto(MultipartFile image) {}
