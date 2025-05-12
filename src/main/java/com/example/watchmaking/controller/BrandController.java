package com.example.watchmaking.controller;

import com.example.watchmaking.dto.brand.BrandCreateDto;
import com.example.watchmaking.dto.brand.BrandUpdateDto;
import com.example.watchmaking.entity.Brand;
import com.example.watchmaking.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @PostMapping
    public ResponseEntity<String> createBrand(@Valid @RequestBody BrandCreateDto brandCreateDto) {
        brandService.CreateBrand(brandCreateDto);
        return ResponseEntity.ok("Marca criada com sucesso!");
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable String brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Brand>> listBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Brand> brands = brandService.listBrands(PageRequest.of(page, size));
        return ResponseEntity.ok(brands);
    }

    @PatchMapping("/update/{brandId}")
    public ResponseEntity<Void> updateBrand(
            @PathVariable String brandId,
            @Valid @RequestBody BrandUpdateDto brandUpdateDto) {
        brandService.updateBrand(UUID.fromString(brandId), brandUpdateDto);
        return ResponseEntity.ok().build();
    }
}
