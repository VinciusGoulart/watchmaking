package com.example.watchmaking.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileSystemStorageManagerTest {

    @Autowired
    private FileSystemStorageManager storageManager;

    @Value("${file.upload-dir.images-watch}")
    private String uploadDir;

    @Test
    void shouldCreateDirectoryIfNotExists() throws IOException {
        Path path = Paths.get(uploadDir);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        storageManager.createDirectoryIfNotExists();

        assertTrue(Files.exists(path), "O diretório deve existir após a criação");
        assertTrue(Files.isDirectory(path), "O caminho deve ser um diretório");
    }
}