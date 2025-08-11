package com.huseyinsen.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFileStorageService {

    // Belirli bir ürüne ait görselleri kaydeder, kayıtlı dosya adlarını döner
    List<String> saveFiles(Long productId, List<MultipartFile> files) throws IOException;

    // Belirli bir dosyayı siler
    void deleteFile(String filename) throws IOException;

    // Dosya adından dosya içeriğini döner (opsiyonel)
    byte[] loadFileAsBytes(String filename) throws IOException;

    String storeFile(MultipartFile file);
}