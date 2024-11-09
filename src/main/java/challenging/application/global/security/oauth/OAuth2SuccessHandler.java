package challenging.application.global.security.oauth;

import challenging.application.domain.auth.constant.AuthConstant;
import challenging.application.domain.auth.entity.Member;
import challenging.application.domain.auth.entity.RefreshToken;
import challenging.application.global.TokenStorage;
import challenging.application.global.security.utils.jwt.JWTUtils;
import challenging.application.domain.auth.repository.MemberRepository;
import challenging.application.domain.auth.service.RefreshTokenService;
import challenging.application.global.security.utils.servletUtils.cookie.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import static challenging.application.domain.auth.constant.AuthConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtils jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2UserImpl customUserDetails = (OAuth2UserImpl) authentication.getPrincipal();

        String uuid = customUserDetails.getUUID();

        String role = getRole(authentication);

        Optional<Member> findMember = memberRepository.findByUuid(uuid);
        // Optional 처리
        if (findMember.isEmpty()) {
            log.error("Member with UUID {} not found.", uuid);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }

        Optional<RefreshToken> findRefreshToken = refreshTokenService.findRefreshToken(findMember.get().getId());

        String refreshToken = null;

        if (findRefreshToken.isEmpty()) {
            refreshToken = jwtUtil.generateRefreshToken(uuid, role);
            refreshTokenService.addRefreshEntity(refreshToken, uuid, jwtUtil.getRefreshExpiredTime());
        } else {
            refreshToken = findRefreshToken.get().getToken();
        }

        String accessToken = jwtUtil.generateAccessToken(uuid, role);



        log.info("Access = {}", accessToken);
        log.info("Refresh = {}", refreshToken);

        // TokenStorage에 accessToken 저장
        TokenStorage.accessToken = AuthConstant.BEARER + accessToken;
        //log.info("TokenStorage에 저장된 Bearer Token: {}", TokenStorage.accessToken);

        setInformationInResponse(response, accessToken, refreshToken);
    }

    private void setInformationInResponse(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        Cookie access = CookieUtils.createCookie(ACCESS_TOKEN, accessToken);
        Cookie refresh = CookieUtils.createCookie(REFRESH_TOKEN, refreshToken);

        response.addCookie(access);
        response.addCookie(refresh);

        response.setStatus(HttpStatus.OK.value());

        // 리디렉션 URL 확인
        String redirectUrl = "http://localhost:8080/";
        //log.info("Redirecting to URL: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private String getRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        return auth.getAuthority();
    }
}
