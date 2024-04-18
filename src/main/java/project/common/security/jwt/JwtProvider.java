package project.common.security.jwt;

import project.domain.user.domain.User;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.service.RedisService;
import project.common.security.oauth.UserInfo;
import project.domain.user.dao.UserRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

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


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    //만료시간 : 30분 -> 개발 기간동안 24시간으로 변경
    private final Long ACCESS_EXP_TIME = 1000L * 60 * 60 * 24;
    
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
        
        Claims claims = validateRefreshToken(refreshToken);
        
        String email = (String)claims.get("email");
        
        User user = userRepository.findByEmail(email)
                                    .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        return createAccessToken(user);
    }
    
    // User정보로 Access Token 생성
    public String createAccessToken(User user){
        
		Long userId = user.getId();
		
        String email = user.getEmail();
        
        String authorities = user.getRoleKey();
        
        Date expiration = new Date(System.currentTimeMillis() + ACCESS_EXP_TIME);
        
        return Jwts.builder()
				.claim("userId", userId + "")
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
        
        Claims claims = validateAccessToken(accessToken);
		
		String userId =  (String)claims.get("userId");
        
        String email = (String)claims.get("email");
        
        String authority = (String)claims.get("authorities");
        
        UserInfo principal = UserInfo.builder()
										.userId(Long.parseLong(userId))
                                        .username(email)
                                        .authority(authority)
                                        .build();
        
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities()); //principal, credential, authorities

    }
    
    private Claims validateAccessToken(String accessToken){
        
        Claims claims = parseClaims(accessToken);
        
        if (claims.get("userId") == null || claims.get("email") == null || claims.get("authorities") == null){
            throw new PlantException(ErrorCode.TOKEN_INVALID);
        }
        
        return claims;
    }
    
    private Claims validateRefreshToken(String refreshToken){
        
        Claims claims = parseClaims(refreshToken);
        
        if (claims.get("email") == null){
            throw new PlantException(ErrorCode.TOKEN_INVALID);
        }
        
        return claims;
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
        
        throw new PlantException(ErrorCode.TOKEN_INVALID);
    }
    
    private String removeBearer(String tokenString){
        if (!tokenString.startsWith("Bearer")){
            throw new PlantException(ErrorCode.TOKEN_INVALID, "Token not starts with Bearer");
        }
        
        return tokenString.replace("Bearer ", "");
    }
}