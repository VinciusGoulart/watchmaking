package com.example.watchmaking.controller;

import com.example.watchmaking.dto.storage.StorageInsertDto;
import com.example.watchmaking.entity.Storage;
import com.example.watchmaking.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.UUID;

@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/upload-image-watch/{watchUuid}")
    public ResponseEntity<Storage> save(@PathVariable UUID watchUuid ,@ModelAttribute StorageInsertDto file) {
        Storage storage = storageService.saveFile(watchUuid, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(storage);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Storage> getByUuid(@PathVariable String uuid) {
        Storage storage = storageService.findByUuid(UUID.fromString(uuid));
        return ResponseEntity.ok(storage);
    }

    @PutMapping("/upload-image-watch/{uuid}")
    public ResponseEntity<Storage> update(@PathVariable String uuid, @ModelAttribute StorageInsertDto file) {
        Storage storage = storageService.updateFile(UUID.fromString(uuid), file);
        return ResponseEntity.ok(storage);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Storage> delete(@PathVariable String uuid) {
        storageService.deleteFile(UUID.fromString(uuid));
        return ResponseEntity.noContent().build();
    }
}
