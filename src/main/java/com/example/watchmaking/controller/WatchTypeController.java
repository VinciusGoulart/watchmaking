package com.example.watchmaking.controller;

import com.example.watchmaking.dto.watchType.CreateWatchTypeDto;
import com.example.watchmaking.dto.watchType.ListWatchType;
import com.example.watchmaking.dto.watchType.SelectWatchType;
import com.example.watchmaking.dto.watchType.UpdateWatchTypeDto;
import com.example.watchmaking.entity.WatchType;
import com.example.watchmaking.service.WatchTypeService;
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
@RequestMapping("watch-types")
public class WatchTypeController {
    @Autowired
    private WatchTypeService watchTypeService;

    @PostMapping
    public ResponseEntity<WatchType> createWatchType(@Valid @RequestBody CreateWatchTypeDto createWatchTypeDto) {
        createWatchTypeDto.setCode(createWatchTypeDto.getCode().toUpperCase());
        watchTypeService.createWatchType(createWatchTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WatchType> findByCode(@PathVariable String code) {
        WatchType watchType = watchTypeService.findWatchTypeByCode(code.toUpperCase());
        return ResponseEntity.ok().body(watchType);
    }

    @GetMapping("/select")
    public ResponseEntity<List<SelectWatchType>> findBySearch( @RequestParam(defaultValue = "") String search) {
        List<SelectWatchType> selectWatchTypeList = watchTypeService.selectWatchType(search);

        return ResponseEntity.ok().body(selectWatchTypeList);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ListWatchType>> listWatchCategories(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<ListWatchType> result = watchTypeService.listBySearch(search, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{code}")
    public ResponseEntity<WatchType> updateWatchType(@PathVariable String code, @Valid @RequestBody UpdateWatchTypeDto updateDto) {
        WatchType watchType = watchTypeService.updateWatchType(code.toUpperCase(), updateDto);
        return ResponseEntity.ok(watchType);
    }

    @DeleteMapping("/code/{code}")
    public ResponseEntity<String> deleteWatchType(@PathVariable String code) {
        watchTypeService.softDeleteWatchType(code.toUpperCase());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
