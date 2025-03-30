package project.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Getter
@AllArgsConstructor
public enum EnvironmentConstant {
    TEST("test"),
    LOCAL("local"),
    DEV("dev"),
    PROD("prod");

    private final String value;

    public static EnvironmentConstant getByValue(String value) {
        return switch (value) {
            case "test" -> TEST;
            case "local" -> LOCAL;
            case "dev" -> DEV;
            case "prod" -> PROD;
            default -> throw new PlantException(ErrorCode.INTERNAL_SERVER_ERROR);
        };
    }
}
