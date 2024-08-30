package com.shopping_cart.service.image;

import com.shopping_cart.dto.ImageDto;
import com.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.model.Image;
import com.shopping_cart.model.Product;
import com.shopping_cart.repository.ImageRepository;
import com.shopping_cart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("No image found with id:" + id);
        });
    }

    @Override
    public List<ImageDto> savedImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                // Save the image to generate an ID
                Image savedImage = imageRepository.save(image);

                // Build the download URL after the image has been saved and has an ID
                String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);

                // Save the image again with the download URL set
                savedImage = imageRepository.save(savedImage);

                // Prepare ImageDto
                ImageDto imageDto = new ImageDto();
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setImageId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage(), e);
            }
        }

        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));

            // Save the updated image
            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage(), e);
        }
    }
}
