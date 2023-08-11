package com.springboot.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationnFilter extends OncePerRequestFilter {

    //lop nay dung de xac thuc ma thong bao JWT
    //cung mot phuong thuc doFilterInternal cho moi yeu cau

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;

    // xử lý xác thực JWT
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get JWT token from http request
     String token = getTokenFromRequest(request);

     //validate token
     if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
         // get username form token
         String username = jwtTokenProvider.getUsername(token);
         //Tải thông tin người dùng và quyền từ cơ sở dữ liệu hoặc nguồn dữ liệu khác thông qua userDetailsService.
         UserDetails userDetails = userDetailsService.loadUserByUsername(username);

         //Tạo một đối tượng UsernamePasswordAuthenticationToken
         //đại diện cho thông tin xác thực người dùng, bao gồm userDetails và quyền của người dùng.
         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                 userDetails,
                 null,
                 userDetails.getAuthorities()
         );
         //Thiết lập thông tin chi tiết về xác thực bằng WebAuthenticationDetailsSource (địa chỉ IP, thông tin trình duyệt, vv.).
         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // để Spring Security hiểu rằng người dùng đã được xác thực.
         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
     }

     /*
     Ví dụ: Nếu bộ lọc xác thực JWT của bạn đã xác minh thành công và bạn không gọi filterChain.doFilter(request, response);,
     thì bộ lọc kiểm tra quyền truy cập sẽ không được gọi và người dùng có thể không được đưa đến trang yêu cầu quyền truy cập.

     Tóm lại, dòng filterChain.doFilter(request, response); đảm bảo rằng luồng xử lý tiếp tục với các bộ lọc
     tiếp theo trong chuỗi bộ lọc của Spring Security để thực hiện các chức năng bảo mật khác.
     * */
     filterChain.doFilter(request,response);

    }

    private String getTokenFromRequest(HttpServletRequest request){

        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());

        }

        return null;

    }



}
