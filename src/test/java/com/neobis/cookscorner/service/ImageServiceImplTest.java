package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.image.ImageOutDto;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.model.Image;
import com.neobis.cookscorner.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ImageServiceImpl imageService;

    private Image image;
    private ImageOutDto imageOutDto;

    @BeforeEach
    void setUp() {
        image = new Image(
                "http://example.com/image.jpg",
                "test-id");
        imageOutDto = new ImageOutDto(
                1L,
                "http://example.com/image.jpg",
                "test-id",
                LocalDateTime.now()
        );
    }

    @Test
    void uploadImage_ShouldReturnImageOutDto() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", image.getImageUrl());
        uploadResult.put("public_id", image.getRemoteId());
        when(cloudinaryService.upload(multipartFile)).thenReturn(uploadResult);
        when(imageRepository.save(any())).thenReturn(image);
        when(modelMapper.map(image, ImageOutDto.class)).thenReturn(imageOutDto);

        ImageOutDto result = imageService.uploadImage(multipartFile);

        verify(cloudinaryService).upload(multipartFile);
        verify(imageRepository).save(image);
        verify(modelMapper).map(image, ImageOutDto.class);
        assertEquals(imageOutDto.getId(), result.getId());
    }

    @Test
    void deleteImageById_ShouldReturnNull() throws IOException {
        when(imageRepository.findById(1L)).thenReturn(Optional.ofNullable(image));

        imageService.deleteImageById(1L);

        verify(cloudinaryService).delete(image.getRemoteId());
        verify(imageRepository).delete(image);
    }

    @Test
    void deleteImageById_ShouldThrowResourceExistsException() throws IOException {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceExistsException.class, () -> imageService.deleteImageById(1L));
    }
}