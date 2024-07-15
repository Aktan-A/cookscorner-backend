package com.neobis.cookscorner.dto.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInDto {

    @NotEmpty(message = "Name is required")
    private String name;

}
