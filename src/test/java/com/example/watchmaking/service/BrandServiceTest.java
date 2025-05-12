package com.example.watchmaking.service;

import com.example.watchmaking.dto.brand.BrandCreateDto;
import com.example.watchmaking.dto.brand.BrandUpdateDto;
import com.example.watchmaking.entity.Brand;
import com.example.watchmaking.repository.BrandRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    BrandCreateDto brandCreateDto;
    Brand brand;
    UUID uuid;

    @BeforeEach
    public void setup() {
        brandCreateDto = new BrandCreateDto("Rolex", "Suíça");
        brand = new Brand(brandCreateDto);
        uuid = UUID.fromString("581aa336-0001-4d86-b409-bfb85bb9eaca");
    }

    @Test
    public void createBrand_Success() {
        when(brandRepository.existsByName(any(String.class))).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenReturn(this.brand);

        // Act
        brandService.CreateBrand(brandCreateDto);

        // Assert
        assertEquals("Rolex" ,brand.getName());
        assertEquals("Suíça", brand.getCountry());
    }

    @Test
    public void createBrand_throwError() {
        when(brandRepository.existsByName(any(String.class))).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> brandService.CreateBrand(brandCreateDto));
    }

    @Test
    public void deleteBrand_Success() {
        when(brandRepository.existsByUuidAndIsDeletedFalse(uuid)).thenReturn(true);
        doNothing().when(brandRepository).deleteByUuid(uuid);

        // Act
        brandService.deleteBrand("581aa336-0001-4d86-b409-bfb85bb9eaca");

        // Assert
        verify(brandRepository, times(1)).existsByUuidAndIsDeletedFalse(uuid);
        verify(brandRepository, times(1)).deleteByUuid(uuid);
        assertTrue(brandRepository.existsByUuidAndIsDeletedFalse(uuid));
    }

    @Test
    public void deleteBrand_createBrand_throwError() {
        when(!brandRepository.existsByUuidAndIsDeletedFalse(uuid)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> brandService.deleteBrand("581aa336-0001-4d86-b409-bfb85bb9eaca"));

        verify(brandRepository, times(1)).existsByUuidAndIsDeletedFalse(uuid);
        assertFalse(brandRepository.existsByUuidAndIsDeletedFalse(uuid));
    }

    @Test
    public void listBrands() {
        PageImpl<Brand> page = new PageImpl<>(List.of(brand));
        Pageable pageable = PageRequest.of(0, 10);

        // Arrange
        when(brandRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<Brand> brands = brandService.listBrands(pageable);

        // Assert
        assertNotNull(brands);
       assertEquals(1, brands.getTotalElements());
       assertEquals(1, brands.getTotalPages());
    }

    @Test
    public void updateBrands_Success() {
        Optional<Brand> brandOptional = Optional.of(new Brand());
        BrandUpdateDto brandUpdateDto = new BrandUpdateDto("Cartier","França", null);
        this.brand = new Brand(UUID.fromString("581aa336-0001-4d86-b409-bfb85bb9eaca"), "Cartier", "França", false, null, null);
        UUID uuid = UUID.fromString("581aa336-0001-4d86-b409-bfb85bb9eaca");
        when(brandRepository.existsByUuid(uuid)).thenReturn(true);
        when(brandRepository.save(any(Brand.class))).thenReturn(this.brand);

        // Act
        brandService.updateBrand(uuid, brandUpdateDto);

        // Assert
        assertEquals("Cartier" ,brand.getName());
        assertEquals("França", brand.getCountry());
        assertFalse(brand.getIsDeleted());
    }

    @Test
    public void updateBrands_ThrowError() {
        when(brandRepository.existsByUuid(uuid)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> brandService.updateBrand(uuid, any(BrandUpdateDto.class)));

        verify(brandRepository, times(1)).existsByUuid(uuid);
    }

}