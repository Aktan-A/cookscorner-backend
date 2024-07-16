package com.neobis.cookscorner.dto.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInDto {

    @NotEmpty(message = "Name is required")
    @Size(max = 200, message = "Name must not be longer than 200 characters")
    private String name;

}
