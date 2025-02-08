package project.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnvironmentConstant {
    LOCAL("local"),
    DEV("dev"),
    PROD("prod");

    private final String value;
}
