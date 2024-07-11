package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.image.ImageOutDto;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.model.Image;
import com.neobis.cookscorner.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public ImageOutDto uploadImage(MultipartFile multipartFile) throws IOException {
        Map result = cloudinaryService.upload(multipartFile);
        String url = result.get("url").toString();
        String remoteId = result.get("public_id").toString();
        Image image = new Image(url, remoteId);
        return modelMapper.map(imageRepository.save(image), ImageOutDto.class);
    }

    @Override
    public void deleteImageById(Long id) throws IOException {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isEmpty()) {
            throw new ResourceExistsException(
                    String.format("Image with id %s was not found.", id));
        }

        Image imageModel = image.get();
        cloudinaryService.delete(imageModel.getRemoteId());
        imageRepository.delete(imageModel);
    }
}
