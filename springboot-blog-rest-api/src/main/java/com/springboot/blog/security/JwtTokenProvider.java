package com.springboot.blog.security;


import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpiration;

    //generate JWT token
    /*
    * Tóm lại, phần mã này thực hiện việc tạo chuỗi JWT bằng cách cấu hình thông tin cần thiết
    * (như tên người dùng, thời gian phát hành, thời gian hết hạn)
    * và sau đó ký chuỗi bằng một khóa HMAC dựa trên jwtSecret để đảm bảo tính toàn vẹn và xác thực của JWT.
    * */
    public String generateToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

       String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
               .signWith(key())
                .compact();

       return token;
    }

    /*
    * Decoders.BASE64.decode(jwtSecret): Giải mã chuỗi jwtSecret từ mã hóa Base64 thành một mảng byte.
    Keys.hmacShaKeyFor(...): Sử dụng mảng byte giải mã để tạo và trả về một khóa HMAC.
    * */
    private Key key(){
       return Keys.hmacShaKeyFor(
               Decoders.BASE64.decode(jwtSecret)
       );
    }

    //lay username tu jwt token
    /*
    * Tóm lại, đoạn mã này thực hiện việc giải mã và trích xuất thông tin từ chuỗi JWT.
    * Nó sử dụng Jwts.parserBuilder() để cấu hình quá trình giải mã bằng cách đặt khóa xác thực,
    * sau đó phân tích chuỗi JWT để lấy thông tin claims từ phần payload,
    * và cuối cùng lấy ra tên người dùng từ thuộc tính "subject" của claims và trả về nó.
    * */
    public String getUsername(String token){
         Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
           String username = claims.getSubject();
           return username;
    }

    //validate Jet token
    /*
    * Phương thức này kiểm tra tính hợp lệ của một chuỗi JWT. Nó thử phân tích cú pháp chuỗi JWT,
    * kiểm tra xem nó có bị hết hạn hay không,
    * và xác minh chữ ký sử dụng khóa bí mật. Nếu có bất kỳ lỗi nào xảy ra, phương thức sẽ ném ra các ngoại lệ tương ứng
    * */
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }

    }
}
