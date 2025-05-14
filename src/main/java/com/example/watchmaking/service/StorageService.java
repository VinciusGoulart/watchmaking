package com.example.watchmaking.service;

import com.example.watchmaking.config.FileSystemStorageManager;
import com.example.watchmaking.dto.storage.StorageInsertDto;
import com.example.watchmaking.dto.watch.WatchUpdateDto;
import com.example.watchmaking.entity.Storage;
import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.repository.StorageRepository;
import com.example.watchmaking.repository.WatchRepository;
import com.example.watchmaking.util.expcetions.InvalidFileTypeException;
import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.SaveFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private FileSystemStorageManager storageManager;

    @Autowired
    private WatchService watchService;

    private static final Set<String> ALLOWED_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"
    ));

    private static final String IMAGE_BASE_URL = "/images/watch/";

    @Transactional
    public Storage saveFile(UUID watchUuid, StorageInsertDto dto) {
        MultipartFile file = dto.getFile();
        try {
            this.validateFile(file);
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

            String fileUrl = IMAGE_BASE_URL + uniqueFileName;

            storageManager.createDirectoryIfNotExists();

            Path filePath = storageManager.getUploadPath().resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            Storage storage = new Storage(
                    null,
                    uniqueFileName,
                    fileUrl,
                    file.getContentType(),
                    file.getSize(),
                    null
            );
            Storage newStorage = storageRepository.save(storage);
            WatchUpdateDto watchDto = new WatchUpdateDto(watchService.findByUuid(watchUuid));
            watchDto.setImageUuid(newStorage.getUuid());
            Watch watch = watchService.updateWatch(watchUuid, newStorage.getUuid(), watchDto);
            return newStorage;
        } catch (IOException e) {
            throw new SaveFileException("Falha ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    public Storage findByUuid(UUID uuid) {
        Optional<Storage> optionalStorage = storageRepository.findByUuid(uuid);
        if (optionalStorage.isEmpty()) {
            throw new NotFoundException("Arquivo não encontrado");
        }
        return optionalStorage.get();
    }

    @Transactional
    public Storage updateFile(UUID uuid, StorageInsertDto dto) {
        Storage existing = storageRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Arquivo não encontrado"));

        MultipartFile file = dto.getFile();
        this.validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        String fileUrl = IMAGE_BASE_URL + uniqueFileName;

        storageManager.createDirectoryIfNotExists();
        Path filePath = storageManager.getUploadPath().resolve(uniqueFileName);

        try {
            Files.copy(file.getInputStream(), filePath);

            if (existing.getFileName() != null) {
                Path oldFilePath = storageManager.getUploadPath().resolve(existing.getFileName());
                Files.deleteIfExists(oldFilePath);
            }

            Storage newStorage = new Storage(existing.getUuid() ,uniqueFileName, fileUrl, file.getContentType(), file.getSize(), null);

            return storageRepository.save(newStorage);

        } catch (IOException e) {
            throw new SaveFileException("Falha ao atualizar o arquivo: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteFile(UUID uuid) {
        Storage storage = storageRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Arquivo não encontrado"));

        Path filePath = storageManager.getUploadPath().resolve(storage.getFileName());
        try {
            Files.deleteIfExists(filePath);
            storageRepository.delete(storage);
        } catch (IOException e) {
            throw new SaveFileException("Falha ao deletar o arquivo: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileTypeException("Arquivo não pode ser nulo");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidFileTypeException("Tipo de arquivo não permitido. Apenas imagens são aceitas (JPEG, PNG, GIF, BMP, WEBP)");
        }
    }

}
