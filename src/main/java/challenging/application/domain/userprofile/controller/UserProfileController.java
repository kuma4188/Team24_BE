package challenging.application.domain.userprofile.controller;

import challenging.application.global.security.annotation.LoginMember;
import challenging.application.domain.auth.entity.Member;
import challenging.application.global.dto.request.UserProfileRequest.UserProfilePutRequest;
import challenging.application.global.dto.response.UserProfileResponse.UserProfileGetResponse;
import challenging.application.global.dto.response.UserProfileResponse.UserProfilePutResponse;
import challenging.application.domain.userprofile.service.UserProfileService;
import challenging.application.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/userprofile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 유저 프로필 조회 페이지 렌더링
    @GetMapping
    public String getUserProfilePage(@LoginMember Member user, Model model) {
        UserProfileGetResponse userProfileResponse = userProfileService.getUserProfile(user.getId());
        model.addAttribute("userProfile", userProfileResponse);
        return "userprofile"; // userprofile.html 템플릿 렌더링
    }

    // 유저 프로필 조회 API
    @GetMapping("/data")
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> getUserProfile(@LoginMember Member user) {
        UserProfileGetResponse userProfileResponse = userProfileService.getUserProfile(user.getId());
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.successResponse(userProfileResponse));
    }

    // 유저 프로필 수정 API
    @PostMapping("/data")
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> updateUserProfile(
        @LoginMember Member user,
        @RequestParam("userNickName") String userNickName,
        @RequestParam(value = "extension", required = false) String extension,
        @RequestParam(value = "image", required = false) MultipartFile image) {
        UserProfilePutRequest userProfilePutRequest = new UserProfilePutRequest(userNickName, extension, image);
        UserProfilePutResponse userProfileResponse = userProfileService.putUserProfile(user.getId(), userProfilePutRequest);
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(ApiResponse.successResponse(userProfileResponse));
    }


}

