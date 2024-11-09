# Team24_BE
24조 백엔드 - 박수빈

## 개발 스프린트 _ 타임리프 생성

# 1. 기본 셋팅
-  


CustomAuthenticationPrincipalArgumentResolver 클래스:

getMemberFromAuthentication 메서드가 Optional<Member>를 반환하도록 수정했습니다. 이를 통해 호출 시 orElseThrow를 사용하여 예외 처리할 수 있도록 했습니다.
Optional 사용을 통해, 인증된 사용자가 데이터베이스에 없는 경우에도 NoSuchElementException이 아닌 명확한 IllegalArgumentException을 던지도록 개선했습니다.
JWTUtils 클래스:

getUUID 메서드가 token으로부터 UUID를 가져오는 메서드명을 일관되게 유지했습니다.
getJWTInformation 메서드는 토큰에서 필요한 정보를 Map으로 반환하며, 개별 클레임을 얻는 메서드들을 재사용하도록 했습니다.


- ㅁㄴㅇ
- ㅁㄴㅇ
- ㅁㄴㅇ

