package project.common.util;

import static project.common.constant.EnvironmentConstant.*;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import project.common.constant.EnvironmentConstant;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {

    private final Environment env;

    public EnvironmentConstant getCurrentProfile() {
        return EnvironmentConstant.getByValue(getCurrentProfileString());
    }

    private String getCurrentProfileString() {
        return Stream.of(env.getActiveProfiles())
                .filter(profile -> profile.equals(TEST.getValue())
                        || profile.equals(LOCAL.getValue())
                        || profile.equals(DEV.getValue())
                        || profile.equals(PROD.getValue()))
                .findFirst()
                .orElseThrow(() -> new PlantException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
