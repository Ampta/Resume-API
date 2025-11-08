package com.ampta.resume_api.util;

public class Endpoints {

    // Authentication endpoints
    public static final String AUTH_CONTROLLER = "/api/auth";
    public static final String REGISTER = "/register";
    public static final String VERIFY_EMAIL = "/verify-email";
    public static final String UPLOAD_IMAGE = "/upload-image";
    public static final String LOGIN = "/login";
    public static final String RESEND_VERIFICATION = "/resend-verification";
    public static final String PROFILE = "/profile";

    // Resume endpoints
    public static final String RESUME_CONTROLLER = "/api/resumes";
    public static final String ID = "/{id}";
    public static final String UPLOAD_IMAGES = "/{id}/upload-images";


    private Endpoints() {}
}
