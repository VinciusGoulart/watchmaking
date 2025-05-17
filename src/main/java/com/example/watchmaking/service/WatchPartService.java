package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchPart.CreateWatchPartDto;
import com.example.watchmaking.dto.watchPart.ListWatchPartDto;
import com.example.watchmaking.dto.watchPart.SelectWatchPartDto;
import com.example.watchmaking.dto.watchPart.UpdateWatchPartDto;
import com.example.watchmaking.entity.WatchPart;
import com.example.watchmaking.repository.WatchPartRepository;
import com.example.watchmaking.util.expcetions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class WatchPartService {
    @Autowired
    private WatchPartRepository watchPartRepository;

    @Transactional
    public WatchPart createWatchPart(CreateWatchPartDto createWatchPartDto) {
        boolean codeExists = watchPartRepository.existsByCodeIgnoreCase(createWatchPartDto.getCode());
        if (codeExists) {
            throw new IllegalArgumentException("Código já está em uso por outra categoria.");
        }

        WatchPart watchPart = new WatchPart(
                createWatchPartDto.getName(),
                createWatchPartDto.getCode().toUpperCase(Locale.ROOT),
                createWatchPartDto.getDescription(),
                createWatchPartDto.getStock(),
                createWatchPartDto.getUnitPrice(),
                createWatchPartDto.getIsDeleted()
        );

        return watchPartRepository.save(watchPart);
    }

    public WatchPart findWatchPartByCode(String code) {
        return watchPartRepository.findByCode(code).orElseThrow(
                () -> new NotFoundException("Categoria com o código " + code + " não encontrada")
        );
    }

    public List<SelectWatchPartDto> selectWatchPart(String search) {
        List<SelectWatchPartDto> selectWatchPartList;

        selectWatchPartList = watchPartRepository.selectBySearch(search);

        return !selectWatchPartList.isEmpty() ? selectWatchPartList : new ArrayList<>();
    }

    public Page<ListWatchPartDto> listBySearch(String search, Pageable pageable) {
        return watchPartRepository.listBySearch(search, pageable);
    }

    @Transactional
    public WatchPart updateWatchPart(String code, UpdateWatchPartDto updateDto) {
        WatchPart existingCategory = this.findWatchPartByCode(code);

        if (updateDto.getCode() != null && !updateDto.getCode().equalsIgnoreCase(code)) {
            boolean codeExists = watchPartRepository.existsByCodeIgnoreCase(updateDto.getCode());
            if (codeExists) {
                throw new IllegalArgumentException("Código já está em uso por outra categoria.");
            }
        }

        WatchPart updated = new WatchPart(existingCategory, updateDto);
        return watchPartRepository.save(updated);
    }

    @Transactional
    public void softDeleteWatchPart(String code) {
        this.findWatchPartByCode(code);

        watchPartRepository.softDeleteByCode(code);
    }
}
