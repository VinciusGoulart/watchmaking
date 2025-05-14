package com.example.watchmaking.service;

import com.example.watchmaking.config.FileSystemStorageManager;
import com.example.watchmaking.dto.storage.StorageInsertDto;
import com.example.watchmaking.dto.watch.WatchUpdateDto;
import com.example.watchmaking.entity.Storage;
import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.repository.StorageRepository;
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
import java.util.stream.Collectors;

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
    public void saveOrUpdateWatchImages(UUID watchUuid, StorageInsertDto dto) {
        Watch watch = watchService.findByUuid(watchUuid);
        List<Storage> existingImages = watch.getImages();

        Set<String> existingOriginalNames = existingImages.stream()
                .map(storage -> removeTimestampPrefix(storage.getFileName()))
                .collect(Collectors.toSet());

        Set<String> incomingOriginalNames = dto.getFile().stream()
                .map(file -> StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                .collect(Collectors.toSet());

        List<Storage> newStorages = new ArrayList<>();

        for (MultipartFile file : dto.getFile()) {
            if (!file.isEmpty() && !file.getName().isEmpty()) {
                this.validateFile(file);


                String originalFileName = StringUtils.cleanPath(
                        Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "_")
                );

                if (!existingOriginalNames.contains(originalFileName)) {
                    String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
                    String fileUrl = IMAGE_BASE_URL + uniqueFileName;

                    try {
                        storageManager.createDirectoryIfNotExists();
                        Path filePath = storageManager.getUploadPath().resolve(uniqueFileName);
                        Files.copy(file.getInputStream(), filePath);

                        Storage storage = new Storage(
                                null,
                                uniqueFileName,
                                fileUrl,
                                file.getContentType(),
                                file.getSize(),
                                null,
                                watch
                        );
                        newStorages.add(storage);
                    } catch (IOException e) {
                        throw new SaveFileException("Falha ao salvar o arquivo: " + e.getMessage(), e);
                    }
                }
            }
        }

        List<Storage> imagesToKeep = existingImages.stream()
                .filter(storage -> incomingOriginalNames.contains(removeTimestampPrefix(storage.getFileName())))
                .toList();

        List<Storage> imagesToRemove = existingImages.stream()
                .filter(storage -> !incomingOriginalNames.contains(removeTimestampPrefix(storage.getFileName())))
                .toList();

        imagesToRemove.forEach((image) -> {
            this.deleteFile(image.getUuid());
        });

        List<Storage> finalImages = new ArrayList<>(imagesToKeep);
        finalImages.addAll(newStorages);

        storageRepository.saveAll(newStorages);

        WatchUpdateDto watchDto = new WatchUpdateDto(watch);

        watchDto.setImages(finalImages);

        watchService.updateWatch(watchUuid, watchDto);

    }


    public Storage findByUuid(UUID uuid) {
        Optional<Storage> optionalStorage = storageRepository.findByUuid(uuid);
        if (optionalStorage.isEmpty()) {
            throw new NotFoundException("Arquivo não encontrado");
        }
        return optionalStorage.get();
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
        String contentType = file.getContentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new InvalidFileTypeException("Tipo de arquivo não permitido. Apenas imagens são aceitas (JPEG, PNG, GIF, BMP, WEBP)");
        }
    }

    private String removeTimestampPrefix(String fileName) {
        int underscoreIndex = fileName.indexOf('_');
        return (underscoreIndex != -1) ? fileName.substring(underscoreIndex + 1) : fileName;
    }
}
