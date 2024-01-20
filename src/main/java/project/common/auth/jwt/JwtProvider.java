package project.common.auth.jwt;

import project.domain.*;
import project.service.RedisService;
import project.common.auth.oauth.UserInfo;
import project.repository.UserRepository;
import project.exception.user.*;
import project.exception.auth.InvalidTokenException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.*;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    //만료시간 : 30분
    private final Long ACCESS_EXP_TIME = 1000L * 60 * 30;
    
    //만료 시간 : 하루
    private final Long REFRESH_EXP_TIME = 1000L * 60 * 60 * 24;
    
    private final UserRepository userRepository;
    private final RedisService redisService;
    
    @Value("${jwt.secret}")
    private String salt;
    
    private Key secretKey;
    
    @PostConstruct
    protected void init(){
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }
    
    //RefreshToken으로 AccesToken 생성
    public String createAccessToken(String refreshToken){
        
        validateRefreshToken(refreshToken);
        
        String email = getEmailByToken(refreshToken);
        
        User user = userRepository.findByEmail(email)
                                    .orElseThrow(NoSuchUserException::new);
        
        return createAccessToken(user);
    }
    
    // User정보로 Access Token 생성
    public String createAccessToken(User user){
        
        String email = user.getEmail();
        
        String authorities = user.getRoleKey();
        
        Date expiration = new Date(System.currentTimeMillis() + ACCESS_EXP_TIME);
        
        return Jwts.builder()
                .claim("email", email)
                .claim("authorities", authorities)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    //User 정보로 Refresh Token 생성
    public String createRefreshToken(User user){
        String email = user.getEmail();
        
        Date expiration = new Date(System.currentTimeMillis() + REFRESH_EXP_TIME);
        
        String refreshToken = Jwts.builder()
                                .claim("email", email)
                                .setExpiration(expiration)
                                .signWith(SignatureAlgorithm.HS256, secretKey)
                                .compact();
        
        //redis 유효기간 설정
        redisService.setValues(email, refreshToken);
        redisService.setExpiration(email, REFRESH_EXP_TIME);
        
        return refreshToken;
    }

    
    //AccessToken -> Authentication 
    public Authentication getAuthentication(String accessToken){
        
        validateAccessToken(accessToken);
        
        String email = getEmailByToken(accessToken);
        
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(NoSuchUserException::new);
        
        UserInfo principal = UserInfo.from(findUser);
        
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());

    }
    
    private void validateAccessToken(String accessToken){
        
        Claims claims = parseClaims(accessToken);
        
        if (claims.get("email") == null || claims.get("authorities") == null){
            throw new InvalidTokenException("토큰 값이 올바르지 않습니다");    
        }
    }
    
    private void validateRefreshToken(String refreshToken){
        
        Claims claims = parseClaims(refreshToken);
        
        if (claims.get("email") == null){
            throw new InvalidTokenException("토큰 값이 올바르지 않습니다");    
        }
    }
    
    private String getEmailByToken(String tokenString){
        Claims claims = parseClaims(tokenString);
        
        return (String) claims.get("email");
    }
    
    private Claims parseClaims(String tokenString){
        try{
            String tokenWithoutBearer = removeBearer(tokenString);
            
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(tokenWithoutBearer)
                .getBody();
        } 
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token");
        } 
        catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } 
        catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } 
        catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.");
        }
        
        throw new InvalidTokenException("올바르지 않은 토큰입니다");
    }
    
    private String removeBearer(String tokenString){
        if (!tokenString.startsWith("Bearer")){
            throw new InvalidTokenException("Token not start with Bearer");
        }
        
        return tokenString.replace("Bearer ", "");
    }
}