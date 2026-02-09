package com.bemicroservices.user_service.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class CloudinaryUtil {
    Cloudinary cloudinary;

    public String upload(MultipartFile file) throws IOException {
        try {
            log.info("Uploading file to Cloudinary: {}", file.getOriginalFilename());
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", "stories_avatars"
                    ));
            String secureUrl = uploadResult.get("secure_url").toString();
            log.info("File uploaded successfully. URL: {}", secureUrl);
            return secureUrl;
        } catch (IOException e) {
            log.error("Failed to upload file to Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }

    public void remove(String url) throws IOException {
        try {
            log.info("Removing file from Cloudinary: {}", url);
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
            log.info("File removed successfully: {}", publicId);
        } catch (IOException e) {
            log.error("Failed to remove file from Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to remove file from Cloudinary: " + e.getMessage(), e);
        }
    }

    private String extractPublicId(String url) {
        String path = url.substring(url.lastIndexOf("stories_avatars/") + "stories_avatars/".length());
        return path.substring(0, path.lastIndexOf("."));
    }
}