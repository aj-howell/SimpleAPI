package com.amigoscode.JWT;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTUtil {
	
	private static final String SECRET_KEY= "foobar_123456789_foobar_123456789_foobar_123456789_foobar_123456789_"; 
	public String issueToken(String Subject, Map<String,Object> claims)
	{
		
		String token= Jwts
		.builder()
		.setClaims(claims)
		.setSubject(Subject)
		.setIssuer("https://amigscode.com")
		.setIssuedAt(Date.from(Instant.now()))
		.setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
		.signWith(getSigningKey(),SignatureAlgorithm.HS256)
		.compact();
		
		return token;
	}
	
	public String issueToken(String Subject)
	{
		return issueToken(Subject,Map.of());
	}
	
	public String issueToken(String Subject, String...scopes)
	{
		return issueToken(Subject,Map.of("scopes",scopes));
	}
	
	public String issueToken(String Subject, List<String> scopes) {
		// TODO Auto-generated method stub
		return issueToken(Subject,Map.of("scopes",scopes));
	}


	private Key getSigningKey()
	{
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		
	}
	
	public String getSubject(String Token)
	{
		Claims claims = getClaims(Token);
		return claims.getSubject();
	}
	
	public Claims getClaims(String token)
	{
		Claims claims =Jwts.parserBuilder()
		.setSigningKey(getSigningKey())
		.build()
		.parseClaimsJws(token)
		.getBody();
		return claims;
	}
	

	public boolean isTokenValid(String jwtToken, String email) {
		String subject = getSubject(jwtToken);
		
		//subject should be equal to the unique email/username
		return subject.equals(email) && !isNotExpired(jwtToken);
	}

	private boolean isNotExpired(String jwtToken) {
		// TODO Auto-generated method stub
		return getClaims(jwtToken)
		.getExpiration()
		.before(Date.from(Instant.now()));
	}

	
}
