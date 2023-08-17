package com.example.bookshop.services;

import com.example.bookshop.model.entity.book.file.BookFile;
import com.example.bookshop.repositories.BookFileRepository;
import liquibase.util.file.FilenameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceStorage {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${download.path}")
    private String downloadPath;

    private final BookFileRepository bookFileRepository;
    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {

        String resourceURI = null;

        if (!file.isEmpty()) {
            if (!new File(uploadPath).exists()) {
                Files.createDirectories(Paths.get(uploadPath));
                log.info("created image folder in {}", uploadPath);
            }
            String fileName = slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath, fileName);
            resourceURI = "/book-covers/" + fileName;
            file.transferTo(path);
            log.info("{} uploaded OK!", fileName);
        }
        return  resourceURI;
    }

    public Path getBookFilePath(String hash) {
        BookFile bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
        return Paths.get(bookFileEntity.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFile bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
        String mimeType =
                URLConnection.guessContentTypeFromName(Paths.get(bookFileEntity.getPath()).getFileName().toString());
        if (mimeType != null) {
            return MediaType.parseMediaType(mimeType);
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFile bookFileEntity = bookFileRepository.findBookFileEntityByHash(hash);
        Path path = Paths.get(downloadPath, bookFileEntity.getPath());
        return Files.readAllBytes(path);

    }
}
