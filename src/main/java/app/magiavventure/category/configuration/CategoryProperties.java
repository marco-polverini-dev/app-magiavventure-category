package app.magiavventure.category.configuration;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "category")
public class CategoryProperties {

    private ErrorProperties errors;

    @Data
    public static class ErrorProperties {
        private Map<String, ErrorMessage> errorMessages = Collections.emptyMap();

        @Data
        @Builder
        public static class ErrorMessage {
            private String message;
            private String description;
            private String code;
            private int status;
        }
    }
}
