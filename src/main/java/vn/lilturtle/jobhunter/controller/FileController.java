package vn.lilturtle.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.lilturtle.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.lilturtle.jobhunter.service.FileService;
import vn.lilturtle.jobhunter.util.annotation.ApiMessage;
import vn.lilturtle.jobhunter.util.error.StorageException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${lilturtle.upload-file.base-uri}")
    private String baseURI;


    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException, StorageException {
        // skip validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please provide a file.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("Invalid file format. Please provide a valid file format: " + allowedExtensions);
        }

        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);
        // store file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok(res);
    }
}