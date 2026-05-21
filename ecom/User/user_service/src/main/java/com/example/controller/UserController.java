package com.example.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.dto.AddressRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UserProfileRequest;
import com.example.dto.UserProfileResponse;
import com.example.dto.UserRequest;
import com.example.dto.UserResponse;
import com.example.entities.Address;
import com.example.entities.User;
import com.example.entities.UserProfile;
import com.example.service.AddressService;
import com.example.service.JwtService;
import com.example.service.UserProfileService;
import com.example.service.UserService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

@Controller("/api/users")
public class UserController {

        private final UserService userService;
        private final JwtService jwtService;
        private final UserProfileService userProfileService;
        private final AddressService addressService;

        public UserController(UserService userService,
                        JwtService jwtService,
                        UserProfileService userProfileService,
                        AddressService addressService) {
                this.userService = userService;
                this.jwtService = jwtService;
                this.userProfileService = userProfileService;
                this.addressService = addressService;
        }

        // =====================
        // GET ALL USERS
        // =====================
        @Get("/")
        public HttpResponse<List<User>> getAllUsers() {
                return HttpResponse.ok(userService.getAll());
        }

        // =====================
        // REGISTER
        // =====================
        @Secured(SecurityRule.IS_ANONYMOUS)
        @Post("/register")
        public HttpResponse<?> register(@Body RegisterRequest request) {

                try {
                        // 1. tạo user
                        User user = userService.createUser(request);

                        // 2. tạo profile
                        UserProfileRequest profile = new UserProfileRequest();
                        profile.setFullName(request.getFullName());
                        profile.setPhone(request.getPhone());

                        userProfileService.createProfile(user.getId(), profile);

                        UserResponse response = new UserResponse();
                        response.setId(user.getId());
                        response.setEmail(user.getEmail());

                        return HttpResponse.created(response);

                } catch (Exception e) {
                        return HttpResponse.badRequest(e.getMessage());
                }
        }

        // =====================
        // LOGIN
        // =====================
        @Secured(SecurityRule.IS_ANONYMOUS)
        @Post("/login")
        public HttpResponse<?> login(@Body UserRequest request) {

                Optional<User> user = userService.login(
                                request.getEmail(),
                                request.getPassword());

                if (user.isPresent()) {
                        String token = jwtService.generateToken(user.get());

                        return HttpResponse.ok(Map.of(
                                        "accessToken", token));
                }

                return HttpResponse.unauthorized();
        }

        // =====================
        // GET CURRENT USER (TOKEN)
        // =====================
        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Get("/me")
        public HttpResponse<?> getCurrentUser(@Header("Authorization") String authHeader) {

                try {
                        String token = authHeader.replace("Bearer ", "");

                        Optional<User> user = userService.getUserFromToken(token);

                        return user
                                        .<HttpResponse<?>>map(HttpResponse::ok)
                                        .orElseGet(HttpResponse::unauthorized);

                } catch (Exception e) {
                        return HttpResponse.badRequest("Invalid token");
                }
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Get("/profiles")
        public HttpResponse<?> getProfile(Authentication authentication) {

                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                UserProfile profile = userProfileService.getByUserId(user.getId())
                                .orElse(new UserProfile());

                List<Address> addresses = addressService.getByUserId(user.getId());

                // sort default lên đầu
                addresses.sort((a, b) -> Boolean.compare(b.getIsDefault(), a.getIsDefault()));

                UserProfileResponse res = new UserProfileResponse();

                // USER INFO
                res.setEmail(user.getEmail());
                res.setFullName(profile.getFullName());
                res.setPhone(profile.getPhone());
                res.setAvatarUrl(profile.getAvatarUrl());

                // ADDRESS LIST
                res.setAddresses(addresses);

                return HttpResponse.ok(res);
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Put("/profiles")
        public HttpResponse<?> updateProfile(
                        Authentication authentication,
                        @Body UserProfileRequest request) {

                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                userProfileService.updateProfile(user.getId(), request);

                return HttpResponse.ok(
                                Map.of(
                                                "message", "Update profile success"));
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Post("/addresses")
        public HttpResponse<?> addAddress(
                        Authentication authentication,
                        @Body AddressRequest request) {

                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                addressService.addAddress(user.getId(), request);

                return HttpResponse.ok(
                                Map.of("message", "Add address success"));
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Put("/addresses/{id}")
        public HttpResponse<?> updateAddress(
                        @PathVariable String id,
                        Authentication authentication,
                        @Body AddressRequest request) {

                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                addressService.updateAddress(user.getId(), id, request);

                return HttpResponse.ok(
                                Map.of("message", "Update address success"));
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Delete("/addresses/{id}")
        public HttpResponse<?> deleteAddress(
                        @PathVariable String id,
                        Authentication authentication) {
                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                addressService.deleteAddress(user.getId(), id);

                return HttpResponse.ok();
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Post(value = "/profiles/avatar", consumes = MediaType.MULTIPART_FORM_DATA)
        public HttpResponse<?> uploadAvatar(
                        Authentication authentication,
                        @Part("file") CompletedFileUpload file) {
                try {
                        // lấy user từ email trong token
                        String email = authentication.getName();

                        User user = userService.findByEmail(email)
                                        .orElseThrow(() -> new RuntimeException("User not found"));

                        String userId = user.getId();

                        // tạo folder
                        String uploadDir = "uploads/avatars/";
                        Files.createDirectories(Paths.get(uploadDir));

                        // tạo file name
                        String fileName = UUID.randomUUID() + "_" + file.getFilename();
                        Path filePath = Paths.get(uploadDir + fileName);

                        // lưu file
                        Files.write(filePath, file.getBytes());

                        String avatarUrl = "/uploads/avatars/" + fileName;

                        userProfileService.updateAvatar(userId, avatarUrl);

                        return HttpResponse.ok(Map.of(
                                        "message", "Upload avatar success",
                                        "avatarUrl", avatarUrl));

                } catch (Exception e) {
                        return HttpResponse.badRequest(e.getMessage());
                }
        }

        @Secured(SecurityRule.IS_AUTHENTICATED)
        @Put("/addresses/{id}/default")
        public HttpResponse<?> setDefaultAddress(
                        @PathVariable String id,
                        Authentication authentication) {

                String email = authentication.getName();

                User user = userService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                addressService.setDefault(user.getId(), id);

                return HttpResponse.ok(
                                Map.of("message", "Default address updated"));
        }
}