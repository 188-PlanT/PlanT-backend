package project.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;

@Getter
@AllArgsConstructor
public enum EnvironmentConstant {
    LOCAL("local"),
    DEV("dev"),
    PROD("prod");

    private final String value;

    public static EnvironmentConstant getByValue(String value) {
        return switch (value) {
            case "local" -> LOCAL;
            case "dev" -> DEV;
            case "PROD" -> PROD;
            default -> throw new PlantException(ErrorCode.INTERNAL_SERVER_ERROR);
        };
    }
}
