package project.infra.s3.application;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.common.constant.UrlConstant;
import project.common.property.S3Property;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Property s3Property;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        // 파일 형식 구하기
        String ext = fileName.split("\\.")[1];
        String contentType = "";

        // content type을 지정해서 올려주지 않으면 자동으로 "application/octet-stream"으로 고정이 되서 링크 클릭시 웹에서 열리는게 아니라 자동 다운이 시작됨.
        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "csv":
                contentType = "text/csv";
                break;
        }

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(multipartFile.getSize());
            amazonS3Client.putObject(
                    new PutObjectRequest(s3Property.getBucket(), fileName, multipartFile.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) { // 여기 예외처리 필요
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return UrlConstant.S3_DEV_URL + "/" + fileName;
    }
}
