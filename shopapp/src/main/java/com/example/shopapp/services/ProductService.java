package com.example.shopapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamExcepttion;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.responses.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    // Giới hạn tối đa số ảnh cho mỗi sản phẩm
    private static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        // Kiểm tra category có tồn tại không
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id = " + productDTO.getCategoryId()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(product -> {
            ProductResponse productResponse = ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .thumbnail(product.getThumbnail())
                    .description(product.getDescription())
                    .categoryId(product.getId())
                    .build();
            productResponse.setCreatedAt(product.getCreatedAt());
            productResponse.setUpdatedAt(product.getUpdatedAt());
            return productResponse;
        });
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);

        // Kiểm tra category mới có tồn tại không
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id = " + productDTO.getCategoryId()));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(existingCategory);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamExcepttion {

        Product existingProduct = getProductById(productImageDTO.getProductId());

        // Kiểm tra số lượng ảnh không vượt quá giới hạn
        int size = productImageRepository.findByProductId(productImageDTO.getProductId()).size();
        if (size >= MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamExcepttion(
                    "Number of images must be <= " + MAXIMUM_IMAGES_PER_PRODUCT);
        }

        ProductImage productImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        return productImageRepository.save(productImage);
    }
}
