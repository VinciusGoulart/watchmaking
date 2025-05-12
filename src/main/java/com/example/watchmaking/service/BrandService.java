package com.example.watchmaking.service;

import com.example.watchmaking.dto.brand.BrandCreateDto;
import com.example.watchmaking.dto.brand.BrandUpdateDto;
import com.example.watchmaking.entity.Brand;
import com.example.watchmaking.repository.BrandRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.ResourceExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Transactional
    public Brand CreateBrand(BrandCreateDto brandCreateDto) {
        if (brandRepository.existsByName(brandCreateDto.getName())) {
            throw new ResourceExistsException("Marca já cadastrada!");
        }
        Brand brand = new Brand(brandCreateDto);
        return brandRepository.save(brand);
    }

    @Transactional
    public void deleteBrand(String brandId) {
        UUID uuid = UUID.fromString(brandId);
        if (!brandRepository.existsByUuidAndIsDeletedFalse(uuid)) {
            throw new NotFoundException("Marca não encontrada!");
        }
        brandRepository.deleteByUuid(uuid);
    }

    public Page<Brand> listBrands(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    public Brand updateBrand(UUID uuid, BrandUpdateDto brandUpdateDto) {
        if(!brandRepository.existsByUuid(uuid)) {
            throw new NotFoundException("Marca não encontrada!");
        }
        Brand newBrand = new Brand(uuid, brandUpdateDto);
        return brandRepository.save(newBrand);
    }

}
