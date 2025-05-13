package com.example.watchmaking.controller;

import com.example.watchmaking.dto.watchCategory.CreateWatchCategoryDto;
import com.example.watchmaking.dto.watchCategory.ListWatchCategory;
import com.example.watchmaking.dto.watchCategory.SelectWatchCategory;
import com.example.watchmaking.dto.watchCategory.UpdateWatchCategoryDto;
import com.example.watchmaking.entity.WatchCategory;
import com.example.watchmaking.service.WatchCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("watch-categories")
public class WatchCategoryController {
    @Autowired
    private WatchCategoryService watchCategoryService;

    @PostMapping
    public ResponseEntity<WatchCategory> createWatchCategory(@Valid @RequestBody CreateWatchCategoryDto createWatchCategoryDto) {
        createWatchCategoryDto.setCode(createWatchCategoryDto.getCode().toUpperCase());
        watchCategoryService.createWatchCategory(createWatchCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WatchCategory> findByCode(@PathVariable String code) {
        WatchCategory watchCategory = watchCategoryService.findWatchCategoryByCode(code.toUpperCase());
        return ResponseEntity.ok().body(watchCategory);
    }

    @GetMapping("/select")
    public ResponseEntity<List<SelectWatchCategory>> findBySearch( @RequestParam(defaultValue = "") String search) {
        List<SelectWatchCategory> selectWatchCategoryList = watchCategoryService.selectWatchCategory(search);

        return ResponseEntity.ok().body(selectWatchCategoryList);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ListWatchCategory>> listWatchCategories(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<ListWatchCategory> result = watchCategoryService.listBySearch(search, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{code}")
    public ResponseEntity<WatchCategory> updateWatchCategory(@PathVariable String code, @Valid @RequestBody UpdateWatchCategoryDto updateDto) {
        WatchCategory watchCategory = watchCategoryService.updateWatchCategory(code.toUpperCase(), updateDto);
        return ResponseEntity.ok(watchCategory);
    }

    @DeleteMapping("/code/{code}")
    public ResponseEntity<String> deleteWatchCategory(@PathVariable String code) {
        watchCategoryService.softDeleteWatchCategory(code.toUpperCase());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
