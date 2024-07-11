package com.neobis.cookscorner.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageOutDto {

    private Long id;
    private String imageUrl;
    private String remoteId;
    private LocalDateTime createdAt;

}
