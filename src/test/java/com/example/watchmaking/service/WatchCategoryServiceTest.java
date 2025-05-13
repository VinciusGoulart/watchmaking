package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchCategory.CreateWatchCategoryDto;
import com.example.watchmaking.dto.watchCategory.ListWatchCategory;
import com.example.watchmaking.dto.watchCategory.SelectWatchCategory;
import com.example.watchmaking.dto.watchCategory.UpdateWatchCategoryDto;
import com.example.watchmaking.entity.WatchCategory;
import com.example.watchmaking.repository.WatchCategoryRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchCategoryServiceTest {

    @Mock
    private WatchCategoryRepository watchCategoryRepository;

    @InjectMocks
    private WatchCategoryService watchCategoryService;

    private CreateWatchCategoryDto dto;

    @BeforeEach
    void setUp() {
        dto = new CreateWatchCategoryDto("Luxo", "LUXURY");
    }

    @Test
    void createWatchCategory_Success() {
        // Arrange
        when(watchCategoryRepository.existsByCodeIgnoreCase("LUXURY")).thenReturn(false);
        when(watchCategoryRepository.save(any(WatchCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        WatchCategory created = watchCategoryService.createWatchCategory(dto);

        // Assert
        assertEquals("LUXURY", created.getCode());
        assertEquals("Luxo", created.getName());
        assertFalse(created.getIsDeleted());

        verify(watchCategoryRepository).existsByCodeIgnoreCase("LUXURY");
        verify(watchCategoryRepository).save(any(WatchCategory.class));
    }

    @Test
    void createWatchCategory_CodeAlreadyExists_ShouldThrow() {
        // Arrange
        when(watchCategoryRepository.existsByCodeIgnoreCase("LUXURY")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchCategoryService.createWatchCategory(dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchCategoryRepository).existsByCodeIgnoreCase("LUXURY");
        verify(watchCategoryRepository, never()).save(any());
    }

    @Test
    void findWatchCategoryByCode_Success() {
        // Arrange
        String code = "LUXURY";
        WatchCategory mockCategory = new WatchCategory("Luxo", code.toLowerCase(), false);

        when(watchCategoryRepository.findByCode(code)).thenReturn(Optional.of(mockCategory));

        // Act
        WatchCategory found = watchCategoryService.findWatchCategoryByCode(code);

        // Assert
        assertEquals("luxury", found.getCode());
        assertEquals("Luxo", found.getName());

        verify(watchCategoryRepository).findByCode(code);
    }

    @Test
    void findWatchCategoryByCode_NotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchCategoryRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                watchCategoryService.findWatchCategoryByCode(code));

        assertEquals("Categoria com o código INVALID não encontrada", ex.getMessage());
        verify(watchCategoryRepository).findByCode(code);
    }

    @Test
    void selectWatchCategory_WithMatchingResults_ShouldReturnList() {
        // Arrange
        String search = "lux";

        SelectWatchCategory projection = new SelectWatchCategory();
        projection.setCode("LUXURY");
        projection.setName("Luxo");

        when(watchCategoryRepository.selectBySearch(search))
                .thenReturn(List.of(projection));

        // Act
        List<SelectWatchCategory> result = watchCategoryService.selectWatchCategory(search);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Luxo", result.get(0).getName());
        assertEquals("LUXURY", result.get(0).getCode());
        verify(watchCategoryRepository).selectBySearch(search);
    }


    @Test
    void selectWatchCategory_NoResults_ShouldReturnEmptyList() {
        // Arrange
        when(watchCategoryRepository.selectBySearch(any())).thenReturn(Collections.emptyList());

        // Act
        List<SelectWatchCategory> result = watchCategoryService.selectWatchCategory("sem resultado");

        // Assert
        assertTrue(result.isEmpty());
        verify(watchCategoryRepository).selectBySearch(any());
    }

    @Test
    void listBySearch_WithResults_ShouldReturnPagedList() {
        // Arrange
        String search = "lux";
        Pageable pageable = PageRequest.of(0, 10);

        ListWatchCategory dto = new ListWatchCategory();
        dto.setUuid(UUID.randomUUID());
        dto.setName("Luxo");
        dto.setCode("LUXURY");
        dto.setIsDeleted(false);

        Page<ListWatchCategory> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(watchCategoryRepository.listBySearch(search, pageable)).thenReturn(page);

        // Act
        Page<ListWatchCategory> result = watchCategoryService.listBySearch(search, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Luxo", result.getContent().get(0).getName());
        assertEquals("LUXURY", result.getContent().get(0).getCode());
        assertFalse(result.getContent().get(0).getIsDeleted());

        verify(watchCategoryRepository).listBySearch(search, pageable);
    }

    @Test
    void listBySearch_NoResults_ShouldReturnEmptyPage() {
        // Arrange
        String search = "nada";
        Pageable pageable = PageRequest.of(0, 10);

        Page<ListWatchCategory> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(watchCategoryRepository.listBySearch(search, pageable)).thenReturn(emptyPage);

        // Act
        Page<ListWatchCategory> result = watchCategoryService.listBySearch(search, pageable);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(watchCategoryRepository).listBySearch(search, pageable);
    }



    @Test
    void updateWatchCategory_Success_WithCodeChange() {
        // Arrange
        String oldCode = "LUXURY";
        String newCode = "SPORT";
        UpdateWatchCategoryDto dto = new UpdateWatchCategoryDto("Esportivo", newCode,false);
        WatchCategory existing = new WatchCategory("Luxo", oldCode, false);

        when(watchCategoryRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchCategoryRepository.existsByCodeIgnoreCase(newCode)).thenReturn(false);
        when(watchCategoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchCategory result = watchCategoryService.updateWatchCategory(oldCode, dto);

        // Assert
        assertEquals("Esportivo", result.getName());
        assertEquals("SPORT", result.getCode());
        verify(watchCategoryRepository).findByCode(oldCode);
        verify(watchCategoryRepository).existsByCodeIgnoreCase(newCode);
        verify(watchCategoryRepository).save(any());
    }

    @Test
    void updateWatchCategory_CodeConflict_ShouldThrow() {
        // Arrange
        String oldCode = "LUXURY";
        String newCode = "SPORT";
        UpdateWatchCategoryDto dto = new UpdateWatchCategoryDto("Esportivo", newCode,false);
        WatchCategory existing = new WatchCategory("Luxo", oldCode, false);

        when(watchCategoryRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchCategoryRepository.existsByCodeIgnoreCase(newCode)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchCategoryService.updateWatchCategory(oldCode, dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchCategoryRepository).existsByCodeIgnoreCase(newCode);
        verify(watchCategoryRepository, never()).save(any());
    }

    @Test
    void updateWatchCategory_SameCode_ShouldSkipDuplicateCheck() {
        // Arrange
        String code = "LUXURY";
        UpdateWatchCategoryDto dto = new UpdateWatchCategoryDto("Luxo Atualizado", code,false);
        WatchCategory existing = new WatchCategory("Luxo", code, false);

        when(watchCategoryRepository.findByCode(code)).thenReturn(Optional.of(existing));
        when(watchCategoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchCategory result = watchCategoryService.updateWatchCategory(code, dto);

        // Assert
        assertEquals("Luxo Atualizado", result.getName());
        assertEquals("LUXURY", result.getCode());
        verify(watchCategoryRepository, never()).existsByCodeIgnoreCase(code);
        verify(watchCategoryRepository).save(any());
    }

    @Test
    void softDeleteUserByEmail_Success() {
        // Arrange
        String code = "LUXURY";
        WatchCategory existing = new WatchCategory("Luxo", code, false);

        when(watchCategoryRepository.findByCode(code)).thenReturn(Optional.of(existing));

        // Act
        watchCategoryService.softDeleteWatchCategory(code);

        // Assert
        verify(watchCategoryRepository, times(1)).findByCode(code);
        verify(watchCategoryRepository, times(1)).softDeleteByCode(code);
    }
    
    @Test
    void softDeleteUserByCode_UserNotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchCategoryRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> watchCategoryService.softDeleteWatchCategory(code));
    }

}
