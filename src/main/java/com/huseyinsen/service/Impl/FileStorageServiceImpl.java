package com.huseyinsen.service.Impl;

import com.huseyinsen.service.IFileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements IFileStorageService {

    private final Path storagePath = Paths.get("uploads");

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "png", "webp");

    @PostConstruct
    public void init() throws IOException {
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
    }

    @Override
    public List<String> saveFiles(Long productId, List<MultipartFile> files) throws IOException {
        List<String> savedFilenames = new ArrayList<>();

        for (MultipartFile file : files) {
            // Dosya boyut kontrolü
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IOException("File size exceeds 5MB: " + file.getOriginalFilename());
            }

            // Uzantı kontrolü
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                throw new IOException("Unsupported file type: " + originalFilename);
            }

            // Benzersiz dosya adı, productId de isimde var
            String newFilename = UUID.randomUUID() + "_" + productId + "." + extension;
            Path filePath = storagePath.resolve(newFilename);

            // Dosyayı kaydet
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            savedFilenames.add(newFilename);
        }

        return savedFilenames;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Path filePath = storagePath.resolve(filename);
        Files.deleteIfExists(filePath);
    }

    @Override
    public byte[] loadFileAsBytes(String filename) throws IOException {
        Path filePath = storagePath.resolve(filename);
        return Files.readAllBytes(filePath);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}