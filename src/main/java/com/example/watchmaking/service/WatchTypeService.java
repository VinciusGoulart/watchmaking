package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchType.CreateWatchTypeDto;
import com.example.watchmaking.dto.watchType.ListWatchType;
import com.example.watchmaking.dto.watchType.SelectWatchType;
import com.example.watchmaking.dto.watchType.UpdateWatchTypeDto;
import com.example.watchmaking.entity.WatchType;
import com.example.watchmaking.repository.WatchTypeRepository;
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
public class WatchTypeService {
    @Autowired
    private WatchTypeRepository watchCategoryRepository;

    @Transactional
    public WatchType createWatchType(CreateWatchTypeDto createWatchTypeDto) {
        boolean codeExists = watchCategoryRepository.existsByCodeIgnoreCase(createWatchTypeDto.getCode());
        if (codeExists) {
            throw new IllegalArgumentException("Código já está em uso por outra categoria.");
        }

        WatchType watchCategory = new WatchType(
                createWatchTypeDto.getName(),
                createWatchTypeDto.getCode().toUpperCase(Locale.ROOT),
                false
        );

        return watchCategoryRepository.save(watchCategory);
    }

    public WatchType findWatchTypeByCode(String code) {
        return watchCategoryRepository.findByCode(code).orElseThrow(
                () -> new NotFoundException("Categoria com o código " + code + " não encontrada")
        );
    }

    public List<SelectWatchType> selectWatchType(String search) {
        List<SelectWatchType> selectWatchTypeList;

        selectWatchTypeList = watchCategoryRepository.selectBySearch(search);

        return !selectWatchTypeList.isEmpty() ? selectWatchTypeList : new ArrayList<>();
    }

    public Page<ListWatchType> listBySearch(String search, Pageable pageable) {
        return watchCategoryRepository.listBySearch(search, pageable);
    }

    @Transactional
    public WatchType updateWatchType(String code, UpdateWatchTypeDto updateDto) {
        WatchType existingCategory = this.findWatchTypeByCode(code);

        if (updateDto.getCode() != null && !updateDto.getCode().equalsIgnoreCase(code)) {
            boolean codeExists = watchCategoryRepository.existsByCodeIgnoreCase(updateDto.getCode());
            if (codeExists) {
                throw new IllegalArgumentException("Código já está em uso por outra categoria.");
            }
        }

        WatchType updated = new WatchType(updateDto, existingCategory);
        return watchCategoryRepository.save(updated);
    }

    @Transactional
    public void softDeleteWatchType(String code) {
        this.findWatchTypeByCode(code);

        watchCategoryRepository.softDeleteByCode(code);
    }
}
