package project.infra.s3.application;

import com.amazonaws.services.s3.model.*;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadFile(MultipartFile multipartFile) throws IOException;
}
