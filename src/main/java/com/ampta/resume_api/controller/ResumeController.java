package com.ampta.resume_api.controller;

import com.ampta.resume_api.document.Resume;
import com.ampta.resume_api.dto.CreateResumeRequest;
import com.ampta.resume_api.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.ampta.resume_api.util.Endpoints.*;

@RestController
@RequestMapping(RESUME_CONTROLLER)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request){
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getUserResumes(){
        return null;
    }

    @GetMapping(ID)
    public ResponseEntity<?> getResumeById(@PathVariable String id){
        return null;
    }

    @PutMapping(ID)
    public ResponseEntity<?> updateResume(@PathVariable String id,
                                          @RequestBody Resume updateData){
        return null;
    }

    @PutMapping(UPLOAD_IMAGES)
    public ResponseEntity<?> uploadResumeImages(
            @PathVariable String id,
            @RequestPart(value = "thumbnail", required = true) MultipartFile thumbnail,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request){

        return null;

    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable String id){
        return null;
    }

}
