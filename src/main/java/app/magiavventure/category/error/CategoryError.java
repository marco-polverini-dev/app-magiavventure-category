package app.magiavventure.category.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryError {
    private String key;
    private Throwable throwable;
    private Object[] args;
}
