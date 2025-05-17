package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchPart.CreateWatchPartDto;
import com.example.watchmaking.dto.watchPart.ListWatchPartDto;
import com.example.watchmaking.dto.watchPart.SelectWatchPartDto;
import com.example.watchmaking.dto.watchPart.UpdateWatchPartDto;
import com.example.watchmaking.entity.WatchPart;
import com.example.watchmaking.repository.WatchPartRepository;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchPartServiceTest {

    @Mock
    private WatchPartRepository watchPartRepository;

    @InjectMocks
    private WatchPartService watchPartService;

    private CreateWatchPartDto dto;

    @BeforeEach
    void setUp() {
        dto = new CreateWatchPartDto("Vidro", "GLASS", "Vidro de safira", 10, BigDecimal.valueOf(100), false);
    }

    @Test
    void createWatchPart_Success() {
        // Arrange
        when(watchPartRepository.existsByCodeIgnoreCase("GLASS")).thenReturn(false);
        when(watchPartRepository.save(any(WatchPart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        WatchPart created = watchPartService.createWatchPart(dto);

        // Assert
        assertEquals("GLASS", created.getCode());
        assertEquals("Vidro", created.getName());
        assertFalse(created.getIsDeleted());

        verify(watchPartRepository).existsByCodeIgnoreCase("GLASS");
        verify(watchPartRepository).save(any(WatchPart.class));
    }

    @Test
    void createWatchPart_CodeAlreadyExists_ShouldThrow() {
        // Arrange
        when(watchPartRepository.existsByCodeIgnoreCase("GLASS")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchPartService.createWatchPart(dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchPartRepository).existsByCodeIgnoreCase("GLASS");
        verify(watchPartRepository, never()).save(any());
    }

    @Test
    void findWatchPartByCode_Success() {
        // Arrange
        String code = "GLASS";
        WatchPart mockPart = new WatchPart("Vidro", code, "Vidro de safira",
                10, BigDecimal.valueOf(100), false);

        when(watchPartRepository.findByCode(code)).thenReturn(Optional.of(mockPart));

        // Act
        WatchPart found = watchPartService.findWatchPartByCode(code);

        // Assert
        assertEquals("GLASS", found.getCode());
        assertEquals("Vidro", found.getName());

        verify(watchPartRepository).findByCode(code);
    }

    @Test
    void findWatchPartByCode_NotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchPartRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                watchPartService.findWatchPartByCode(code));

        assertEquals("Categoria com o código INVALID não encontrada", ex.getMessage());
        verify(watchPartRepository).findByCode(code);
    }

    @Test
    void selectWatchPart_WithMatchingResults_ShouldReturnList() {
        // Arrange
        String search = "lux";

        SelectWatchPartDto projection = new SelectWatchPartDto();
        projection.setCode("GLASS");
        projection.setName("Vidro");

        when(watchPartRepository.selectBySearch(search))
                .thenReturn(List.of(projection));

        // Act
        List<SelectWatchPartDto> result = watchPartService.selectWatchPart(search);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vidro", result.get(0).getName());
        assertEquals("GLASS", result.get(0).getCode());
        verify(watchPartRepository).selectBySearch(search);
    }


    @Test
    void selectWatchPart_NoResults_ShouldReturnEmptyList() {
        // Arrange
        when(watchPartRepository.selectBySearch(any())).thenReturn(Collections.emptyList());

        // Act
        List<SelectWatchPartDto> result = watchPartService.selectWatchPart("sem resultado");

        // Assert
        assertTrue(result.isEmpty());
        verify(watchPartRepository).selectBySearch(any());
    }

    @Test
    void listBySearch_WithResults_ShouldReturnPagedList() {
        // Arrange
        String search = "lux";
        Pageable pageable = PageRequest.of(0, 10);

        ListWatchPartDto dto = new ListWatchPartDto();
        dto.setUuid(UUID.randomUUID());
        dto.setName("Vidro");
        dto.setCode("GLASS");
        dto.setIsDeleted(false);

        Page<ListWatchPartDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(watchPartRepository.listBySearch(search, pageable)).thenReturn(page);

        // Act
        Page<ListWatchPartDto> result = watchPartService.listBySearch(search, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Vidro", result.getContent().get(0).getName());
        assertEquals("GLASS", result.getContent().get(0).getCode());
        assertFalse(result.getContent().get(0).getIsDeleted());

        verify(watchPartRepository).listBySearch(search, pageable);
    }

    @Test
    void listBySearch_NoResults_ShouldReturnEmptyPage() {
        // Arrange
        String search = "nada";
        Pageable pageable = PageRequest.of(0, 10);

        Page<ListWatchPartDto> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(watchPartRepository.listBySearch(search, pageable)).thenReturn(emptyPage);

        // Act
        Page<ListWatchPartDto> result = watchPartService.listBySearch(search, pageable);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(watchPartRepository).listBySearch(search, pageable);
    }


    @Test
    void updateWatchPart_Success_WithCodeChange() {
        // Arrange
        String oldCode = "GLASS";
        String newCode = "BUTTON";
        UpdateWatchPartDto dto = new UpdateWatchPartDto("Botao", newCode, "Botao de acrilico",
                10, BigDecimal.valueOf(100), false);
        WatchPart existing = new WatchPart("Vidro", oldCode, "Vidro de safira",
                10, BigDecimal.valueOf(100), false);

        when(watchPartRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchPartRepository.existsByCodeIgnoreCase(newCode)).thenReturn(false);
        when(watchPartRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchPart result = watchPartService.updateWatchPart(oldCode, dto);

        // Assert
        assertEquals("Botao", result.getName());
        assertEquals("BUTTON", result.getCode());
        verify(watchPartRepository).findByCode(oldCode);
        verify(watchPartRepository).existsByCodeIgnoreCase(newCode);
        verify(watchPartRepository).save(any());
    }

    @Test
    void updateWatchPart_CodeConflict_ShouldThrow() {
        // Arrange
        String oldCode = "GLASS";
        String newCode = "BUTTON";
        UpdateWatchPartDto dto = new UpdateWatchPartDto("Botao", newCode, "Botao de acrilico",
                10, BigDecimal.valueOf(100), false);
        WatchPart existing = new WatchPart("Vidro", oldCode, "Vidro de safira",
                10, BigDecimal.valueOf(100), false);

        when(watchPartRepository.findByCode(oldCode)).thenReturn(Optional.of(existing));
        when(watchPartRepository.existsByCodeIgnoreCase(newCode)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                watchPartService.updateWatchPart(oldCode, dto));

        assertEquals("Código já está em uso por outra categoria.", ex.getMessage());
        verify(watchPartRepository).existsByCodeIgnoreCase(newCode);
        verify(watchPartRepository, never()).save(any());
    }

    @Test
    void updateWatchPart_SameCode_ShouldSkipDuplicateCheck() {
        // Arrange
        String code = "GLASS";
        UpdateWatchPartDto dto = new UpdateWatchPartDto("Botao", code, "Botao de acrilico",
                10, BigDecimal.valueOf(100), false);
        WatchPart existing = new WatchPart("Vidro", code, "Vidro de safira",
                10, BigDecimal.valueOf(100), false);

        when(watchPartRepository.findByCode(code)).thenReturn(Optional.of(existing));
        when(watchPartRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        WatchPart result = watchPartService.updateWatchPart(code, dto);

        // Assert
        assertEquals("Botao", result.getName());
        assertEquals("GLASS", result.getCode());
        verify(watchPartRepository, never()).existsByCodeIgnoreCase(code);
        verify(watchPartRepository).save(any());
    }

    @Test
    void softDeleteUserByEmail_Success() {
        // Arrange
        String code = "GLASS";
        WatchPart existing = new WatchPart("Vidro", code, "Vidro de safira",
                10, BigDecimal.valueOf(100), false);

        when(watchPartRepository.findByCode(code)).thenReturn(Optional.of(existing));

        when(watchPartRepository.findByCode(code)).thenReturn(Optional.of(existing));

        // Act
        watchPartService.softDeleteWatchPart(code);

        // Assert
        verify(watchPartRepository, times(1)).findByCode(code);
        verify(watchPartRepository, times(1)).softDeleteByCode(code);
    }

    @Test
    void softDeleteUserByCode_UserNotFound_ShouldThrow() {
        // Arrange
        String code = "INVALID";
        when(watchPartRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> watchPartService.softDeleteWatchPart(code));
    }

}
