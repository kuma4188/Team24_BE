package challenging.application.domain.userprofile.service;

import challenging.application.domain.userprofile.domain.UserProfile;
import challenging.application.domain.userprofile.repository.UserProfileRepository;
import challenging.application.global.dto.request.UserProfileRequest.UserProfilePutRequest;
import challenging.application.global.dto.response.userprofile.UserProfileGetResponse;
import challenging.application.global.dto.response.userprofile.UserProfilePutResponse;
import challenging.application.global.images.S3PresignedImageService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final S3PresignedImageService s3PresignedImageService;

    public UserProfileService(UserProfileRepository userProfileRepository,
        S3PresignedImageService s3PresignedImageService) {
        this.userProfileRepository = userProfileRepository;
        this.s3PresignedImageService = s3PresignedImageService;
    }

    public UserProfileGetResponse getUserProfile(Long memberId) {
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new UserProfileNotFoundException(
                "User profile not found for member ID: " + memberId));

        String presignedGetUrl = null;
        if (userProfile.getImageExtension() != null) {
            presignedGetUrl = s3PresignedImageService.createUserPresignedGetUrl(
                userProfile.getImageExtension(), userProfile.getMember().getUuid()
            );
        }

        return UserProfileGetResponse.of(userProfile, presignedGetUrl);
    }

    public UserProfilePutResponse putUserProfile(Long memberId, UserProfilePutRequest userProfilePutRequest) {
        UserProfile userProfile = userProfileRepository.findByMemberId(memberId)
            .orElseThrow(() -> new UserProfileNotFoundException(
                "User profile not found for member ID: " + memberId));

        // 닉네임과 이미지 확장자 업데이트
        userProfile.updateUserNickName(userProfilePutRequest.userNickName());
        userProfile.updateImageExtension(userProfilePutRequest.extension());

        // Presigned URL 생성
        String presignedPutUrl = null;
        if (userProfilePutRequest.extension() != null) {
            presignedPutUrl = s3PresignedImageService.createUserPresignedPutUrl(
                userProfilePutRequest.extension(), userProfile.getMember().getUuid());
        }

        // 변경 사항 저장
        userProfileRepository.save(userProfile);

        return new UserProfilePutResponse(userProfile.getUserNickName(), presignedPutUrl);
    }



    public static class UserProfileNotFoundException extends RuntimeException {
        public UserProfileNotFoundException(String message) {
            super(message);
        }
    }
}


