package com.example.watchmaking.controller;

import com.example.watchmaking.dto.watch.WatchCreateDto;
import com.example.watchmaking.dto.watch.WatchViewDto;
import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.service.WatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watch")
public class WatchController {
    @Autowired
    private WatchService watchService;

    @PostMapping
    public ResponseEntity<Void> createWatch(@Valid @RequestBody WatchCreateDto dto) {
        watchService.createWatch(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<WatchViewDto>> listWatch(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
//        Page<WatchViewDto> watches = watchService.listWatch(pageable);
        return ResponseEntity.ok(watchService.listWatch(pageable));
    }
}
