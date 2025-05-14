package com.example.watchmaking.config;

import com.example.watchmaking.util.expcetions.CreateDiretoryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileSystemStorageManager {

    @Value("${file.upload-dir.images-watch}")
    private String uploadDir;

    @PostConstruct
    public void init() {
            createDirectoryIfNotExists();
    }

    public void createDirectoryIfNotExists()  {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new CreateDiretoryException("Erro ao criar diretório de upload", e);
        }
    }

    public Path getUploadPath() {
        return Paths.get(uploadDir);
    }
}