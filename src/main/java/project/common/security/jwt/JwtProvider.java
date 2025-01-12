package project.common.security.jwt;

import org.springframework.stereotype.Service;
import project.domain.user.domain.User;
import project.common.exception.ErrorCode;
import project.common.exception.PlantException;
import project.common.service.RedisService;
import project.domain.auth.domain.UserInfo;
import project.domain.user.dao.UserRepository;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.*;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import project.domain.user.domain.UserRole;
import project.domain.workspace.dao.UserWorkspaceRepository;


@Slf4j
@RequiredArgsConstructor
@Service
public class JwtProvider {
    //만료시간 : 30분 -> 개발 기간동안 24시간으로 변경
    private final Long ACCESS_EXP_TIME = 1000L * 60 * 60 * 24;
    
    //만료 시간 : 하루
    private final Long REFRESH_EXP_TIME = 1000L * 60 * 60 * 24;
    
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final RedisService redisService;
    
    @Value("${jwt.secret}")
    private String salt;
    
    private Key secretKey;
    
    @PostConstruct
    protected void init(){
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }
    
    //RefreshToken으로 AccesToken 생성
    public String createAccessTokenByRefreshToken(String refreshToken){
        
        Claims claims = validateRefreshToken(refreshToken);
        
        String email = (String)claims.get("email");
        
        User user = userRepository.findByEmail(email)
                                    .orElseThrow(() -> new PlantException(ErrorCode.USER_NOT_FOUND));
        
        return createAccessTokenByUser(user);
    }

    /**
     * LoginService 내에서 호출됩니다. @Transactional을 사용하지 않습니다.
     */
    public String createAccessTokenByUser(User user){
        
		Long userId = user.getId();
		
        String email = user.getEmail();
        
        String authorities = user.getRoleKey();

        List<Long> workspaceAdminIds = new ArrayList <>();

        List<Long> workspaceUserIds = new ArrayList<>();

        userWorkspaceRepository.searchByUser(user).forEach(
                uw -> {
                    if(uw.getUserRole().equals(UserRole.ADMIN)){
                        workspaceAdminIds.add(uw.getWorkspace().getId());
                    }else if(uw.getUserRole().equals(UserRole.USER)){
                        workspaceUserIds.add(uw.getWorkspace().getId());
                    }
                }
        );

        Date expiration = new Date(System.currentTimeMillis() + ACCESS_EXP_TIME);

        return Jwts.builder()
				.claim("userId", userId + "")
                .claim("email", email)
                .claim("authorities", authorities)
                .claim("workspaceAdminIds", workspaceAdminIds)
                .claim("workspaceUserIds", workspaceUserIds)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    //User 정보로 Refresh Token 생성
    public String createRefreshTokenByUser(User user){
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

        UserInfo principal = UserInfo.from(claims);
        
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities()); //principal, credential, authorities

    }
    
    private Claims validateAccessToken(String accessToken){
        
        Claims claims = parseClaims(accessToken);
        
        if (claims.get("userId") == null ||
                claims.get("email") == null ||
                claims.get("authorities") == null ||
                claims.get("workspaceAdminIds") == null ||
                claims.get("workspaceUserIds") == null
        ){
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