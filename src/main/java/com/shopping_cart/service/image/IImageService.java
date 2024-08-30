package com.shopping_cart.service.image;

import com.shopping_cart.dto.ImageDto;
import com.shopping_cart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> savedImage(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId);

}
