package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.image.ImageOutDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    ImageOutDto uploadImage(MultipartFile multipartFile) throws IOException;

    void deleteImageById(Long id) throws IOException;

}
