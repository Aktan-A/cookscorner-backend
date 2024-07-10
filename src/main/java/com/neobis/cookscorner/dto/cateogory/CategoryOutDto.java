package com.neobis.cookscorner.dto.cateogory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryOutDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
