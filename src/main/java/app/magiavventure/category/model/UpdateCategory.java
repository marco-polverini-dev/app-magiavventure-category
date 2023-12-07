package app.magiavventure.category.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class UpdateCategory {

    @NotNull
    private UUID id;
    private String name;
    private String background;
    private Boolean active;

}
