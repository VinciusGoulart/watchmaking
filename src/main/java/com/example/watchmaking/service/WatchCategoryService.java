package com.example.watchmaking.service;

import com.example.watchmaking.dto.watchCategory.CreateWatchCategoryDto;
import com.example.watchmaking.dto.watchCategory.ListWatchCategory;
import com.example.watchmaking.dto.watchCategory.SelectWatchCategory;
import com.example.watchmaking.dto.watchCategory.UpdateWatchCategoryDto;
import com.example.watchmaking.entity.WatchCategory;
import com.example.watchmaking.repository.WatchCategoryRepository;
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
public class WatchCategoryService {
    @Autowired
    private WatchCategoryRepository watchCategoryRepository;

    @Transactional
    public WatchCategory createWatchCategory(CreateWatchCategoryDto createWatchCategoryDto) {
        boolean codeExists = watchCategoryRepository.existsByCodeIgnoreCase(createWatchCategoryDto.getCode());
        if (codeExists) {
            throw new IllegalArgumentException("Código já está em uso por outra categoria.");
        }

        WatchCategory watchCategory = new WatchCategory(
                createWatchCategoryDto.getName(),
                createWatchCategoryDto.getCode().toUpperCase(Locale.ROOT),
                false
        );

        return watchCategoryRepository.save(watchCategory);
    }

    public WatchCategory findWatchCategoryByCode(String code) {
        return watchCategoryRepository.findByCode(code).orElseThrow(
                () -> new NotFoundException("Categoria com o código " + code + " não encontrada")
        );
    }

    public List<SelectWatchCategory> selectWatchCategory(String search) {
        List<SelectWatchCategory> selectWatchCategoryList;

        selectWatchCategoryList = watchCategoryRepository.selectBySearch(search);

        return !selectWatchCategoryList.isEmpty() ? selectWatchCategoryList : new ArrayList<>();
    }

    public Page<ListWatchCategory> listBySearch(String search, Pageable pageable) {
        return watchCategoryRepository.listBySearch(search, pageable);
    }

    @Transactional
    public WatchCategory updateWatchCategory(String code, UpdateWatchCategoryDto updateDto) {
        WatchCategory existingCategory = this.findWatchCategoryByCode(code);

        if (updateDto.getCode() != null && !updateDto.getCode().equalsIgnoreCase(code)) {
            boolean codeExists = watchCategoryRepository.existsByCodeIgnoreCase(updateDto.getCode());
            if (codeExists) {
                throw new IllegalArgumentException("Código já está em uso por outra categoria.");
            }
        }

        WatchCategory updated = new WatchCategory(updateDto, existingCategory);
        return watchCategoryRepository.save(updated);
    }

    @Transactional
    public void softDeleteWatchCategory(String code) {
        this.findWatchCategoryByCode(code);

        watchCategoryRepository.softDeleteByCode(code);
    }
}
