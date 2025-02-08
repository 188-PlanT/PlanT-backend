package project.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import project.common.constant.EnvironmentConstant;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import static project.common.constant.EnvironmentConstant.*;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EnvironmentUtil {

    private final Environment env;

    public EnvironmentConstant getCurrentProfile(){
        return EnvironmentConstant.valueOf(
                getActiveProfiles()
                    .filter(profile -> profile.equals(LOCAL.getValue()) || profile.equals(DEV.getValue()) || profile.equals(PROD.getValue()))
                    .findFirst()
                    .orElseThrow(() -> new PlantException(ErrorCode.INTERNAL_SERVER_ERROR)));
    }

    private Stream<String> getActiveProfiles(){
        return Stream.of(env.getActiveProfiles());
    }
}
