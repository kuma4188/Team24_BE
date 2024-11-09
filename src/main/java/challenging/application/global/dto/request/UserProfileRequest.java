package challenging.application.global.dto.request;


import org.springframework.web.multipart.MultipartFile;

public record UserProfileRequest() {
    public record UserProfilePutRequest(String userNickName, String extension, MultipartFile image) {

        public String Extension() {
            return  extension;
        }
    }


}
