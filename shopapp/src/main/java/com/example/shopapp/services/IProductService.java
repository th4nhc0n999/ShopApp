package com.example.shopapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamExcepttion;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductResponse;

public interface IProductService {

    // Tạo mới một sản phẩm
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    // Lấy sản phẩm theo id
    Product getProductById(Long id) throws DataNotFoundException;

    // Lấy danh sách sản phẩm có phân trang
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);

    // Cập nhật sản phẩm theo id
    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    // Xóa sản phẩm theo id
    void deleteProduct(Long id);

    // Kiểm tra sản phẩm đã tồn tại theo tên chưa
    boolean existsByName(String name);

    // Thêm ảnh cho sản phẩm
    ProductImage createProductImage(ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamExcepttion;
}
