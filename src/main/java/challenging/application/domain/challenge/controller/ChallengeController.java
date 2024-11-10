package challenging.application.domain.challenge.controller;

import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.challenge.service.ChallengeService;
import challenging.application.global.dto.request.ChallengeRequest;
import challenging.application.global.dto.response.chalenge.ChallengeCreateResponse;
import challenging.application.global.dto.response.chalenge.ChallengeDeleteResponse;
import challenging.application.global.dto.response.chalenge.ChallengeGetResponse;
import challenging.application.global.dto.response.chalenge.ChallengeReservationResponse;
import challenging.application.global.security.annotation.LoginMember;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    // 챌린지 단건 조회
    @GetMapping("/{challengeId}")
    public String getChallenge(@PathVariable("challengeId") Long challengeId, Model model) {
        ChallengeGetResponse response = challengeService.getChallengeById(challengeId);
        model.addAttribute("result", List.of(response)); // 단건 조회 결과도 리스트로 반환
        return "challenge"; // challenge.html 반환
    }



    // 전체 챌린지 조회
    @GetMapping
    public String getChallengesByCategory(Model model) {
        List<ChallengeGetResponse> responses = challengeService.getChallengesByCategoryAndDate();
        model.addAttribute("result", responses);
        return "challenge";
    }

    // 챌린지 생성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createChallenge(
        @RequestPart(value = "dto") ChallengeRequest challengeRequestDTO,
        @RequestParam("upload") MultipartFile multipartFile,
        Model model) {

        ChallengeCreateResponse response = challengeService.createChallenge(challengeRequestDTO, multipartFile);
        model.addAttribute("result", response);
        return "challenge";
    }

    // 챌린지 삭제
    @DeleteMapping("{challengeId}")
    public String deleteChallenge(@PathVariable("challengeId") Long challengeId, @LoginMember Member loginMember, Model model) {
        ChallengeDeleteResponse response = challengeService.deleteChallenge(challengeId, loginMember);
        model.addAttribute("result", response);
        return "challenge";
    }

    // 챌린지 예약
    @PostMapping("/reservation/{challengeId}")
    public String reserveChallenge(@PathVariable("challengeId") Long challengeId, @LoginMember Member loginMember, Model model) {
        ChallengeReservationResponse challengeResponse = challengeService.reserveChallenge(challengeId, loginMember);
        model.addAttribute("result", challengeResponse);
        return "challenge";
    }
}
