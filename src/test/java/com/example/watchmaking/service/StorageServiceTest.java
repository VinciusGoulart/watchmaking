package com.example.watchmaking.service;

import com.example.watchmaking.config.FileSystemStorageManager;
import com.example.watchmaking.dto.storage.StorageInsertDto;
import com.example.watchmaking.entity.Storage;
import com.example.watchmaking.entity.Watch;
import com.example.watchmaking.repository.StorageRepository;
import com.example.watchmaking.util.expcetions.InvalidFileTypeException;
import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.SaveFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private FileSystemStorageManager storageManager;

    @InjectMocks
    private StorageService storageService;

    @Mock
    private WatchService watchService;

    private final Path mockPath = Paths.get("src/main/resources/static/images/watch");
    private static final String BASE_PATH = "/images/watch/";

    @Test
    @DisplayName("Deve salvar arquivo com sucesso para diferentes tipos de imagem")
    void shouldSaveFileSuccessfully() throws IOException {
        UUID uuid = UUID.randomUUID();
        when(storageManager.getUploadPath()).thenReturn(mockPath);
        // Arrange
        String contentType = "image/png";
        String fileName = "test-image.jpg";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                contentType,
                "test image content".getBytes()
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Storage expectedStorage = new Storage(
                null,
                uniqueFileName,
                BASE_PATH + uniqueFileName,
                contentType,
                file.getSize(),
                null
        );

        when(storageRepository.save(any(Storage.class))).thenReturn(expectedStorage);

        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            // Mock Files.copy
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class)))
                    .thenReturn(1L);

            // Act
            Watch mockWatch = mock(Watch.class);
            when(watchService.findByUuid(uuid)).thenReturn(mockWatch);
            Storage result = storageService.saveFile(uuid, dto);

            // Assert
            assertNotNull(result);
            assertEquals(expectedStorage.getMimeType(), result.getMimeType());
            assertTrue(result.getFileName().endsWith(fileName));
            assertTrue(result.getUrl().startsWith(BASE_PATH));

            // Verify
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageRepository).save(any(Storage.class));
            filesMockedStatic.verify(() -> Files.copy(any(InputStream.class), any(Path.class)));
        }
    }

    @Test
    @DisplayName("Deve lançar InvalidFileTypeException quando arquivo for nulo")
    void shouldThrowExceptionWhenFileIsNull() {
        UUID uuid = UUID.randomUUID();
        // Arrange
        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(null);

        // Act & Assert
        InvalidFileTypeException exception = assertThrows(
                InvalidFileTypeException.class,
                () -> storageService.saveFile(uuid,dto)
        );
        assertEquals("Arquivo não pode ser nulo", exception.getMessage());

        // Verify
        verify(storageRepository, never()).save(any());
        verify(storageManager, never()).createDirectoryIfNotExists();
    }

    @Test
    @DisplayName("Deve lançar InvalidFileTypeException quando arquivo estiver vazio")
    void shouldThrowExceptionWhenFileIsEmpty() {
        UUID uuid = UUID.randomUUID();
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "",
                "image/jpeg",
                new byte[0]
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(emptyFile);

        // Act & Assert
        InvalidFileTypeException exception = assertThrows(
                InvalidFileTypeException.class,
                () -> storageService.saveFile(uuid, dto)
        );
        assertEquals("Arquivo não pode ser nulo", exception.getMessage());

        // Verify
        verify(storageRepository, never()).save(any());
        verify(storageManager, never()).createDirectoryIfNotExists();
    }

    @Test
    @DisplayName("Deve lançar InvalidFileTypeException para tipo de arquivo não permitido")
    void shouldThrowExceptionForInvalidFileType() {
        UUID uuid = UUID.randomUUID();
        // Arrange
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(pdfFile);

        // Act & Assert
        InvalidFileTypeException exception = assertThrows(
                InvalidFileTypeException.class,
                () -> storageService.saveFile(uuid, dto)
        );
        assertEquals("Tipo de arquivo não permitido. Apenas imagens são aceitas (JPEG, PNG, GIF, BMP, WEBP)",
                exception.getMessage());

        // Verify
        verify(storageRepository, never()).save(any());
        verify(storageManager, never()).createDirectoryIfNotExists();
    }

    @Test
    @DisplayName("Deve criar URL correta para o arquivo")
    void shouldCreateCorrectFileUrl() throws IOException {
        UUID uuid = UUID.randomUUID();
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        // Arrange
        String fileName = "test-image.jpg";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "image/jpeg",
                "test content".getBytes()
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        Watch mockWatch = mock(Watch.class);
        when(watchService.findByUuid(uuid)).thenReturn(mockWatch);
        when(storageRepository.save(any(Storage.class))).thenAnswer(invocation -> {
            Storage savedStorage = invocation.getArgument(0);
            return savedStorage;
        });

        // Act
        Storage result = storageService.saveFile(uuid, dto);

        // Assert
        assertNotNull(result.getUrl());
        assertTrue(result.getUrl().startsWith("/images/watch/"));
        assertTrue(result.getUrl().endsWith(fileName));
    }



    @Test
    @DisplayName("Deve lançar SaveFileException quando ocorrer erro de IO")
    void shouldThrowSaveFileExceptionOnIOError() throws IOException {
        UUID uuid = UUID.randomUUID();
        // Arrange
        String contentType = "image/jpeg";
        String fileName = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                contentType,
                "test content".getBytes()
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        // Mock do path
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        // Mock do Files.copy para lançar IOException
        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class)))
                    .thenThrow(new IOException("Erro ao copiar arquivo"));

            // Act & Assert
            SaveFileException exception = assertThrows(
                    SaveFileException.class,
                    () -> storageService.saveFile(uuid, dto)
            );

            // Verifica a mensagem de erro
            assertEquals("Falha ao salvar o arquivo: Erro ao copiar arquivo", exception.getMessage());

            // Verify
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageManager).getUploadPath();
            verify(storageRepository, never()).save(any(Storage.class));
        }
    }

    @Test
    @DisplayName("Deve lançar SaveFileException quando houver erro ao salvar no repositório")
    void shouldThrowSaveFileExceptionOnRepositoryError() throws IOException {
        UUID uuid = UUID.randomUUID();
        // Arrange
        String contentType = "image/jpeg";
        String fileName = "test.jpg";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                contentType,
                "test content".getBytes()
        );

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        // Mock do path e comportamento do repositório
        when(storageManager.getUploadPath()).thenReturn(mockPath);
        when(storageRepository.save(any(Storage.class)))
                .thenThrow(new RuntimeException("Erro ao salvar no banco de dados"));

        // Mock do Files.copy
        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class)))
                    .thenReturn(1L);

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> storageService.saveFile(uuid, dto)
            );

            // Verifica a mensagem de erro
            assertEquals("Erro ao salvar no banco de dados", exception.getMessage());

            // Verify
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageManager).getUploadPath();
            verify(storageRepository).save(any(Storage.class));

            // Verifica que o Files.copy foi chamado
            filesMockedStatic.verify(() -> Files.copy(any(InputStream.class), any(Path.class)));
        }
    }

    @Test
    @DisplayName("Deve retornar arquivo quando encontrado por UUID")
    void shouldReturnFileWhenFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage expectedStorage = new Storage(
                uuid,
                "test-image.jpg",
                "/images/watch/test-image.jpg",
                "image/jpeg",
                1024L,
                LocalDateTime.now()
        );

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(expectedStorage));

        // Act
        Storage result = storageService.findByUuid(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStorage.getUuid(), result.getUuid());
        assertEquals(expectedStorage.getFileName(), result.getFileName());
        assertEquals(expectedStorage.getUrl(), result.getUrl());
        assertEquals(expectedStorage.getMimeType(), result.getMimeType());
        assertEquals(expectedStorage.getSize(), result.getSize());

        // Verify
        verify(storageRepository).findByUuid(uuid);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando arquivo não for encontrado")
    void shouldThrowNotFoundExceptionWhenFileNotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storageService.findByUuid(uuid)
        );

        assertEquals("Arquivo não encontrado", exception.getMessage());

        // Verify
        verify(storageRepository).findByUuid(uuid);
    }

    @Test
    @DisplayName("Deve atualizar arquivo com sucesso")
    void shouldUpdateFileSuccessfully() throws IOException {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage existing = new Storage(uuid, "old.jpg", "/images/watch/old.jpg", "image/jpeg", 100L, null);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "new-image.png",
                "image/png",
                "new image content".getBytes()
        );
        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        Storage expected = new Storage(uuid, "any", "any", "image/png", file.getSize(), null);
        when(storageRepository.save(any(Storage.class))).thenReturn(expected);

        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class))).thenReturn(1L);
            filesMockedStatic.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

            // Act
            Storage result = storageService.updateFile(uuid, dto);

            // Assert
            assertNotNull(result);
            assertEquals("image/png", result.getMimeType());
            verify(storageRepository).findByUuid(uuid);
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageManager, atLeastOnce()).getUploadPath();
            verify(storageRepository).save(any(Storage.class));
            filesMockedStatic.verify(() -> Files.copy(any(InputStream.class), any(Path.class)));
            filesMockedStatic.verify(() -> Files.deleteIfExists(any(Path.class)));
        }
    }

    @Test
    @DisplayName("Deve lançar NotFoundException se arquivo não existir")
    void shouldThrowNotFoundExceptionIfFileNotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        StorageInsertDto dto = new StorageInsertDto();
        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> storageService.updateFile(uuid, dto)
        );
        assertEquals("Arquivo não encontrado", exception.getMessage());
        verify(storageRepository).findByUuid(uuid);
        verify(storageManager, never()).createDirectoryIfNotExists();
    }

    @Test
    @DisplayName("Deve lançar InvalidFileTypeException se arquivo for inválido")
    void shouldThrowInvalidFileTypeExceptionIfFileInvalid() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage existing = new Storage(uuid, "old.jpg", "/images/watch/old.jpg", "image/jpeg", 100L, null);

        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(null);

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(existing));

        // Act & Assert
        InvalidFileTypeException exception = assertThrows(
                InvalidFileTypeException.class,
                () -> storageService.updateFile(uuid, dto)
        );
        assertEquals("Arquivo não pode ser nulo", exception.getMessage());
        verify(storageRepository).findByUuid(uuid);
        verify(storageManager, never()).createDirectoryIfNotExists();
    }

    @Test
    @DisplayName("Deve lançar SaveFileException se ocorrer IOException ao copiar arquivo")
    void shouldThrowSaveFileExceptionOnCopyError() throws IOException {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage existing = new Storage(uuid, "old.jpg", "/images/watch/old.jpg", "image/jpeg", 100L, null);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "new-image.png",
                "image/png",
                "new image content".getBytes()
        );
        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class)))
                    .thenThrow(new IOException("Erro ao copiar arquivo"));

            // Act & Assert
            SaveFileException exception = assertThrows(
                    SaveFileException.class,
                    () -> storageService.updateFile(uuid, dto)
            );
            assertTrue(exception.getMessage().contains("Falha ao atualizar o arquivo: Erro ao copiar arquivo"));
            verify(storageRepository).findByUuid(uuid);
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageManager, atLeastOnce()).getUploadPath();
            verify(storageRepository, never()).save(any(Storage.class));
        }
    }

    @Test
    @DisplayName("Deve lançar SaveFileException se ocorrer IOException ao deletar arquivo antigo")
    void shouldThrowSaveFileExceptionOnDeleteError() throws IOException {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage existing = new Storage(uuid, "old.jpg", "/images/watch/old.jpg", "image/jpeg", 100L, null);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "new-image.png",
                "image/png",
                "new image content".getBytes()
        );
        StorageInsertDto dto = new StorageInsertDto();
        dto.setFile(file);

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.copy(any(InputStream.class), any(Path.class))).thenReturn(1L);
            filesMockedStatic.when(() -> Files.deleteIfExists(any(Path.class)))
                    .thenThrow(new IOException("Erro ao deletar arquivo antigo"));

            // Act & Assert
            SaveFileException exception = assertThrows(
                    SaveFileException.class,
                    () -> storageService.updateFile(uuid, dto)
            );
            assertTrue(exception.getMessage().contains("Falha ao atualizar o arquivo: Erro ao deletar arquivo antigo"));
            verify(storageRepository).findByUuid(uuid);
            verify(storageManager).createDirectoryIfNotExists();
            verify(storageManager, atLeastOnce()).getUploadPath();
            verify(storageRepository, never()).save(any(Storage.class));
        }
    }

    @Test
    @DisplayName("Deve deletar arquivo com sucesso")
    void shouldDeleteFileSuccessfully() throws IOException {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Storage storage = new Storage(uuid, "file.jpg", "/images/watch/file.jpg", "image/jpeg", 100L, null);

        when(storageRepository.findByUuid(uuid)).thenReturn(Optional.of(storage));
        when(storageManager.getUploadPath()).thenReturn(mockPath);

        try (MockedStatic<Files> filesMockedStatic = Mockito.mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

            // Act
            storageService.deleteFile(uuid);

            // Assert
            verify(storageRepository).findByUuid(uuid);
            verify(storageManager).getUploadPath();
            filesMockedStatic.verify(() -> Files.deleteIfExists(mockPath.resolve(storage.getFileName())));
            verify(storageRepository).delete(storage);
        }
    }

}