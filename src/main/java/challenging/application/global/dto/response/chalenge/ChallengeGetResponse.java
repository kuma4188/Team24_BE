package challenging.application.global.dto.response.chalenge;


import challenging.application.domain.challenge.entity.Challenge;

public record ChallengeGetResponse(
    Long challengeId,
    String challengeName,
    String challengeBody,
    int point,
    String challengeDate,
    String startTime,
    String endTime,
    String imageUrl,
    int minParticipantNum,
    int maxParticipantNum,
    int currentParticipantNum,
    Long hostId,
    Integer categoryId  // null 가능성을 허용하도록 Integer로 수정
) {

  public static ChallengeGetResponse fromEntity(Challenge challenge, int currentParticipantNum) {
    Integer categoryCode = (challenge.getCategory() != null)
        ? challenge.getCategory().getCategoryCode()
        : null;  // null일 경우 기본값 설정 또는 예외 처리

    return new ChallengeGetResponse(
        challenge.getId(),
        challenge.getName(),
        challenge.getBody(),
        challenge.getPoint(),
        challenge.getDate().toString(),
        challenge.getStartTime().toString(),
        challenge.getEndTime().toString(),
        challenge.getImgUrl(),
        challenge.getMinParticipantNum(),
        challenge.getMaxParticipantNum(),
        currentParticipantNum,
        challenge.getHost().getId(),
        categoryCode  // category가 null일 때를 대비
    );
  }
}
