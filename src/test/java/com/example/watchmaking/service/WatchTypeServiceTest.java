package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchType.CreateWatchTypeDto;
import com.example.watchmaking.dto.watchType.ListWatchType;
import com.example.watchmaking.dto.watchType.SelectWatchType;
import com.example.watchmaking.dto.watchType.UpdateWatchTypeDto;
import com.example.watchmaking.entity.WatchType;
import com.example.watchmaking.repository.WatchTypeRepository;
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
class WatchTypeServiceTest {

    @Mock
    private WatchTypeRepository watchTypeRepository;

    @InjectMocks
    private WatchTypeService watchTypeService;

    private CreateWatchTypeDto dto;

    @BeforeEach
    void setUp() {
        dto = new CreateWatchTypeDto("Luxo", "LUXURY");
    }

    @Test
    void createWatchType_Success() {
        // Arrange
        when(watchTypeRepository.existsByCodeIgnoreCase("LUXURY")).thenReturn(false);
        when(watchTypeRepository.save(any(WatchType.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        WatchType created = watchTypeService.createWatchType(dto);

        // Assert
        assertEquals("LUXURY", created.getCode());
        assertEquals("Luxo", created.getName());
        assertFalse(created.getIsDeleted());

        verify(watchTypeRepository).existsByCodeIgnoreCase("LUXURY");
        verify(watchTypeRepository).save(any(WatchType.class));
    }

    @Test
    void createWatchType_CodeAlreadyExists_ShouldThrow() {
        // Arrange
        when(watchTypeRepository.existsByCodeIgnoreCase("LUXURY")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchTypeService.createWatchType(dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchTypeRepository).existsByCodeIgnoreCase("LUXURY");
        verify(watchTypeRepository, never()).save(any());
    }

    @Test
    void findWatchTypeByCode_Success() {
        // Arrange
        String code = "LUXURY";
        WatchType mockCategory = new WatchType("Luxo", code.toLowerCase(), false);

        when(watchTypeRepository.findByCode(code)).thenReturn(Optional.of(mockCategory));

        // Act
        WatchType found = watchTypeService.findWatchTypeByCode(code);

        // Assert
        assertEquals("luxury", found.getCode());
        assertEquals("Luxo", found.getName());

        verify(watchTypeRepository).findByCode(code);
    }

    @Test
    void findWatchTypeByCode_NotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchTypeRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                watchTypeService.findWatchTypeByCode(code));

        assertEquals("Categoria com o código INVALID não encontrada", ex.getMessage());
        verify(watchTypeRepository).findByCode(code);
    }

    @Test
    void selectWatchType_WithMatchingResults_ShouldReturnList() {
        // Arrange
        String search = "lux";

        SelectWatchType projection = new SelectWatchType();
        projection.setCode("LUXURY");
        projection.setName("Luxo");

        when(watchTypeRepository.selectBySearch(search))
                .thenReturn(List.of(projection));

        // Act
        List<SelectWatchType> result = watchTypeService.selectWatchType(search);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Luxo", result.get(0).getName());
        assertEquals("LUXURY", result.get(0).getCode());
        verify(watchTypeRepository).selectBySearch(search);
    }


    @Test
    void selectWatchType_NoResults_ShouldReturnEmptyList() {
        // Arrange
        when(watchTypeRepository.selectBySearch(any())).thenReturn(Collections.emptyList());

        // Act
        List<SelectWatchType> result = watchTypeService.selectWatchType("sem resultado");

        // Assert
        assertTrue(result.isEmpty());
        verify(watchTypeRepository).selectBySearch(any());
    }

    @Test
    void listBySearch_WithResults_ShouldReturnPagedList() {
        // Arrange
        String search = "lux";
        Pageable pageable = PageRequest.of(0, 10);

        ListWatchType dto = new ListWatchType();
        dto.setUuid(UUID.randomUUID());
        dto.setName("Luxo");
        dto.setCode("LUXURY");
        dto.setIsDeleted(false);

        Page<ListWatchType> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(watchTypeRepository.listBySearch(search, pageable)).thenReturn(page);

        // Act
        Page<ListWatchType> result = watchTypeService.listBySearch(search, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Luxo", result.getContent().get(0).getName());
        assertEquals("LUXURY", result.getContent().get(0).getCode());
        assertFalse(result.getContent().get(0).getIsDeleted());

        verify(watchTypeRepository).listBySearch(search, pageable);
    }

    @Test
    void listBySearch_NoResults_ShouldReturnEmptyPage() {
        // Arrange
        String search = "nada";
        Pageable pageable = PageRequest.of(0, 10);

        Page<ListWatchType> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(watchTypeRepository.listBySearch(search, pageable)).thenReturn(emptyPage);

        // Act
        Page<ListWatchType> result = watchTypeService.listBySearch(search, pageable);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(watchTypeRepository).listBySearch(search, pageable);
    }



    @Test
    void updateWatchType_Success_WithCodeChange() {
        // Arrange
        String oldCode = "LUXURY";
        String newCode = "SPORT";
        UpdateWatchTypeDto dto = new UpdateWatchTypeDto("Esportivo", newCode,false);
        WatchType existing = new WatchType("Luxo", oldCode, false);

        when(watchTypeRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchTypeRepository.existsByCodeIgnoreCase(newCode)).thenReturn(false);
        when(watchTypeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchType result = watchTypeService.updateWatchType(oldCode, dto);

        // Assert
        assertEquals("Esportivo", result.getName());
        assertEquals("SPORT", result.getCode());
        verify(watchTypeRepository).findByCode(oldCode);
        verify(watchTypeRepository).existsByCodeIgnoreCase(newCode);
        verify(watchTypeRepository).save(any());
    }

    @Test
    void updateWatchType_CodeConflict_ShouldThrow() {
        // Arrange
        String oldCode = "LUXURY";
        String newCode = "SPORT";
        UpdateWatchTypeDto dto = new UpdateWatchTypeDto("Esportivo", newCode,false);
        WatchType existing = new WatchType("Luxo", oldCode, false);

        when(watchTypeRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchTypeRepository.existsByCodeIgnoreCase(newCode)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchTypeService.updateWatchType(oldCode, dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchTypeRepository).existsByCodeIgnoreCase(newCode);
        verify(watchTypeRepository, never()).save(any());
    }

    @Test
    void updateWatchType_SameCode_ShouldSkipDuplicateCheck() {
        // Arrange
        String code = "LUXURY";
        UpdateWatchTypeDto dto = new UpdateWatchTypeDto("Luxo Atualizado", code,false);
        WatchType existing = new WatchType("Luxo", code, false);

        when(watchTypeRepository.findByCode(code)).thenReturn(Optional.of(existing));
        when(watchTypeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchType result = watchTypeService.updateWatchType(code, dto);

        // Assert
        assertEquals("Luxo Atualizado", result.getName());
        assertEquals("LUXURY", result.getCode());
        verify(watchTypeRepository, never()).existsByCodeIgnoreCase(code);
        verify(watchTypeRepository).save(any());
    }

    @Test
    void softDeleteUserByEmail_Success() {
        // Arrange
        String code = "LUXURY";
        WatchType existing = new WatchType("Luxo", code, false);

        when(watchTypeRepository.findByCode(code)).thenReturn(Optional.of(existing));

        // Act
        watchTypeService.softDeleteWatchType(code);

        // Assert
        verify(watchTypeRepository, times(1)).findByCode(code);
        verify(watchTypeRepository, times(1)).softDeleteByCode(code);
    }
    
    @Test
    void softDeleteUserByCode_UserNotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchTypeRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> watchTypeService.softDeleteWatchType(code));
    }

}
