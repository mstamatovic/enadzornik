package rs.enadzornik.fileservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Ne mogu da kreiram direktorijum: " + uploadDir, e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Fajl mora imati ime");
            }
            String ext = getFileExtension(originalFilename);
            String fileName = UUID.randomUUID() + "." + ext;
            Path targetLocation = this.rootLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Ne mogu da sačuvam fajl", ex);
        }
    }

    public Path loadFile(String filename) {
        return rootLocation.resolve(filename);
    }

    private String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            throw new RuntimeException("Fajl mora imati ekstenziju");
        }
        String ext = filename.substring(lastDot + 1).toLowerCase();
        if (!"pdf".equals(ext) && !"mp4".equals(ext)) {
            throw new RuntimeException("Dozvoljene ekstenzije: pdf, mp4");
        }
        return ext;
    }
}