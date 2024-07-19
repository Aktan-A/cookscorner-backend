package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.image.ImageOutDto;
import com.neobis.cookscorner.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image Controller", description = "Controller for working with images")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Upload an image", description = "Uploads an image to Cloudinary and saves details in DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully uploaded")
    })
    @PostMapping("/upload")
    public ResponseEntity<ImageOutDto> uploadImage(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(file));
    }

    @Operation(summary = "Delete an image", description = "Deletes an image from Cloudinary and DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Image with provided id was not found")
    })
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImageById(@PathVariable("imageId") Long id) throws IOException {
        imageService.deleteImageById(id);
        return ResponseEntity.ok("Image successfully deleted.");
    }

}
