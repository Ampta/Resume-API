package com.ampta.resume_api.service.impl;

import com.ampta.resume_api.service.FileUploadService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    private final Cloudinary cloudinary;

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {

        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image"));
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }
}
