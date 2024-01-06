package project.common.auth.jwt;

import project.domain.*;
import project.service.RedisService;
import project.common.auth.oauth.UserInfo;
import project.repository.UserRepository;
import project.exception.user.*;
import project.exception.security.UnvalidTokenException;

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
    
    //Access Token 생성
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
    
    //Refresh Token 생성
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
    
    public boolean validateAccessToken(String accessToken){
        
        if (!accessToken.startsWith("Bearer")){
            log.info("Token not start with Bearer");
            return false;
        }
        
        accessToken = accessToken.replace("Bearer ", "");
        
        try{
            Claims claims = parseClaims(accessToken);
            
            if (claims.get("email") != null && claims.get("authorities") != null){
                return true;    
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
    
    //Token -> Authentication 
    //여기서 OAuth2인지 아닌지 구분하는 로직 필요
    public Authentication getAuthentication(String accessToken){
        
        accessToken = accessToken.replace("Bearer ", "");
        
        Claims claims = parseClaims(accessToken);
        
        Collection<? extends GrantedAuthority> authorities = getAuthorityList(claims);
        
        User findUser = userRepository.findByEmail((String) claims.get("email"))
            .orElseThrow(NoSuchUserException::new);
        
        UserInfo principal = UserInfo.from(findUser);
        
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
    
    public boolean validateRefreshToken(String refreshToken){
        
        if (!refreshToken.startsWith("Bearer")){
            log.info("Token not start with Bearer");
            return false;
        }
        
        refreshToken = refreshToken.replace("Bearer ", "");
        
        try{
            Claims claims = parseClaims(refreshToken);
            
            String email = claims.get("email").toString();
            
            if (email != null && redisService.getValues(email) != null){
                return true;    
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
    
    public Claims parseClaims(String tokenString){
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(tokenString)
            .getBody();
    }
    
    private List<GrantedAuthority> getAuthorityList(Claims claims){
        return Arrays.stream(claims.get("authorities").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }
}