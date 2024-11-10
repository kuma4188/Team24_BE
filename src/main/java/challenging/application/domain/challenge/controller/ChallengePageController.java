package challenging.application.domain.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChallengePageController {

    // 챌린지 페이지로 이동
    @GetMapping("/challenge")
    public String challengePage() {
        return "challenge"; // templates/challenge.html을 반환
    }
}
