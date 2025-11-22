package rs.enadzornik.fileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.enadzornik.fileservice.security.JwtUtil;
import rs.enadzornik.fileservice.storage.FileStorageService;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;
    private final JwtUtil jwtUtil;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {

        validateToken(authHeader);
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok(new FileUploadResponse(fileName));
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String filename) throws MalformedURLException {

        validateToken(authHeader);
        Path filePath = fileStorageService.loadFile(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Nedostaje JWT token");
        }
        String token = authHeader.substring(7);
        jwtUtil.getClaims(token); // baca izuzetak ako je nevažeći
    }

    public static class FileUploadResponse {
        private String filePath;

        public FileUploadResponse(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}