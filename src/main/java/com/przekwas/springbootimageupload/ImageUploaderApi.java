package com.przekwas.springbootimageupload;

import com.google.cloud.storage.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController

public class ImageUploaderApi {

    //https://www.programcreek.com/java-api-examples/?api=com.google.cloud.storage.BlobId

    //frontend in Angular in progress

    Storage storage = StorageOptions.getDefaultInstance().getService();

    @GetMapping
    public String get() {
        return "Hello";
    }
    @PostMapping("/upload-file")
    public void get(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        BlobId blobId = BlobId.of("image-uploadrer-4444-bucket",multipartFile.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        storage.create(blobInfo, multipartFile.getBytes());
    }

    @GetMapping("/get-file/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String file) throws IOException {
        Blob blob = storage.get("image-uploader=4444-bucket", file);
        byte[] bytes = storage.readAllBytes("image-uploader=4444-bucket", file);
        Resource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok().contentType(MediaType.valueOf(blob.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"file\"; filename=\"{file}\"".replace("{file}",file))
                .body(resource);
    }
}
