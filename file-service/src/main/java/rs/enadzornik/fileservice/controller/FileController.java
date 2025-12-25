package rs.enadzornik.fileservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.enadzornik.fileservice.security.JwtUtil;
import rs.enadzornik.fileservice.storage.FileStorageService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "http://localhost:8000",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS},
        allowedHeaders = {"Authorization", "Content-Type"})
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
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            // 1. Validacija imena fajla (sprečava "../" napade)
            if (filename.contains("..")) {
                return ResponseEntity.badRequest().build();
            }

            // 2. Učitaj putanju preko postojećeg servisa
            Path filePath = fileStorageService.loadFile(filename);
            Resource resource = new UrlResource(filePath.toUri());

            // 3. Proveri da li fajl postoji i može da se pročita
            if (resource.exists() && resource.isReadable()) {
                // 4. Odredi Content-Type
                String contentType = "application/octet-stream";
                if (filename.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (filename.endsWith(".mp4")) {
                    contentType = "video/mp4";
                }

                // 5. Postavi "inline" umesto "attachment"
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .header("Content-Security-Policy", "frame-ancestors 'self' http://localhost:8000;") // ← KLJUČNO
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Loguj grešku (opciono)
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
//    public ResponseEntity<Resource> downloadFile(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable String filename) throws MalformedURLException {
//
//        validateToken(authHeader);
//        Path filePath = fileStorageService.loadFile(filename);
//        Resource resource = new UrlResource(filePath.toUri());
//
//        if (resource.exists()) {
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//        try {
//            Path file = rootLocation.resolve(filename).normalize();
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() && resource.isReadable()) {
//                String contentType = "application/octet-stream";
//                if (filename.endsWith(".pdf")) {
//                    contentType = "application/pdf";
//                } else if (filename.endsWith(".mp4")) {
//                    contentType = "video/mp4";
//                }
//
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(contentType))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (MalformedURLException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

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