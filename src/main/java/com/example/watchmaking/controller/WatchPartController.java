package com.example.watchmaking.controller;

import com.example.watchmaking.dto.watchPart.CreateWatchPartDto;
import com.example.watchmaking.dto.watchPart.ListWatchPartDto;
import com.example.watchmaking.dto.watchPart.SelectWatchPartDto;
import com.example.watchmaking.dto.watchPart.UpdateWatchPartDto;
import com.example.watchmaking.entity.WatchPart;
import com.example.watchmaking.service.WatchPartService;
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
@RequestMapping("watch-parts")
public class WatchPartController {
    @Autowired
    private WatchPartService watchPartService;

    @PostMapping
    public ResponseEntity<WatchPart> createWatchPart(@Valid @RequestBody CreateWatchPartDto createWatchPartDto) {
        createWatchPartDto.setCode(createWatchPartDto.getCode().toUpperCase());
        watchPartService.createWatchPart(createWatchPartDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WatchPart> findByCode(@PathVariable String code) {
        WatchPart watchPart = watchPartService.findWatchPartByCode(code.toUpperCase());
        return ResponseEntity.ok().body(watchPart);
    }

    @GetMapping("/select")
    public ResponseEntity<List<SelectWatchPartDto>> findBySearch( @RequestParam(defaultValue = "") String search) {
        List<SelectWatchPartDto> selectWatchPartList = watchPartService.selectWatchPart(search);

        return ResponseEntity.ok().body(selectWatchPartList);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ListWatchPartDto>> listWatchCategories(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search
    ) {
        Page<ListWatchPartDto> result = watchPartService.listBySearch(search, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{code}")
    public ResponseEntity<WatchPart> updateWatchPart(@PathVariable String code, @Valid @RequestBody UpdateWatchPartDto updateDto) {
        WatchPart watchPart = watchPartService.updateWatchPart(code.toUpperCase(), updateDto);
        return ResponseEntity.ok(watchPart);
    }

    @DeleteMapping("/code/{code}")
    public ResponseEntity<String> deleteWatchPart(@PathVariable String code) {
        watchPartService.softDeleteWatchPart(code.toUpperCase());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
