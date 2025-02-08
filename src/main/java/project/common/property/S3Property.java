package project.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * application-s3.yml에서 S3관련 정보를 담고 있는 클래스
 * region의 상위 변수 이름이 static인 관계로, @ConfigurationProperties 사용 불가능
 */
@Getter
@Setter
@Component
public class S3Property {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
}
