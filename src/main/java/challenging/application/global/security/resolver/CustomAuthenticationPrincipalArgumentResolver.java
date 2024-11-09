package challenging.application.global.security.resolver;

import challenging.application.global.security.annotation.LoginMember;
import challenging.application.domain.auth.entity.Member;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.domain.auth.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtils jwtUtils;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAnonymous(authentication)) {
            log.info("Authentication 객체가 없거나, 익명 사용자 입니다.");
            return null;
        }

        Member member = getMemberFromAuthentication(authentication)
            .orElseThrow(() -> new IllegalArgumentException("해당 UUID로 사용자를 찾을 수 없습니다."));

        log.info("member name = {}", member.getUsername());
        return member;
    }

    private Optional<Member> getMemberFromAuthentication(Authentication authentication) {
        String token = (String) authentication.getPrincipal();

        String uuid = jwtUtils.getUUID(token);

        return memberRepository.findByUuid(uuid);
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
