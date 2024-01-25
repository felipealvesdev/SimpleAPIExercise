package com.example.stock.controllers;


import com.example.stock.dtos.ProductDTO;
import com.example.stock.model.ProductModel;
import com.example.stock.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct (@RequestBody @Valid ProductDTO productDTO) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDTO, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productModel = productRepository.findById(id);
        if(productModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(productModel.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductById(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productModel = productRepository.findById(id);
        if(productModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        productRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProductById(@PathVariable(value = "id") UUID id,
                                            @RequestBody @Valid ProductDTO productDTO ) {
        Optional<ProductModel> productModel = productRepository.findById(id);
        if(productModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        var product = productModel.get();
        BeanUtils.copyProperties(productDTO, product);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
    }
}
