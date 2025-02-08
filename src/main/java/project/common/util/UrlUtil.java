package project.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import project.common.constant.EnvironmentConstant;
import static project.common.constant.UrlConstant.*;

@Component
@RequiredArgsConstructor
public class UrlUtil {

    private final EnvironmentUtil environmentUtil;
    private final Map<EnvironmentConstant, String> apiUrlMap = Map.of(
            EnvironmentConstant.LOCAL, API_LOCAL_URL,
            EnvironmentConstant.DEV, API_DEV_URL,
            EnvironmentConstant.PROD, API_PROD_URL
    );

    private final Map<EnvironmentConstant, String> frontUrlMap = Map.of(
            EnvironmentConstant.LOCAL, FRONT_LOCAL_URL,
            EnvironmentConstant.DEV, FRONT_DEV_URL,
            EnvironmentConstant.PROD, FRONT_PROD_URL
    );

    private final Map<EnvironmentConstant, String> s3UrlMap= Map.of(
            EnvironmentConstant.LOCAL, S3_DEV_URL,
            EnvironmentConstant.DEV, S3_DEV_URL,
            EnvironmentConstant.PROD, S3_PROD_URL
    );

    public String getApiUrl(){
        return apiUrlMap.get(environmentUtil.getCurrentProfile());
    }

    public String getFrontUrl(){
        return frontUrlMap.get(environmentUtil.getCurrentProfile());
    }
}
