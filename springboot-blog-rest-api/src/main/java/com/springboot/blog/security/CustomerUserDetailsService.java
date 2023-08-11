package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
     User user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /*
    * ạo một đối tượng User từ thông tin người dùng đã tìm thấy.
    * Đối tượng User ở đây là một lớp được cung cấp bởi Spring Security để đại diện cho người dùng
    * trong quá trình xác thực và phân quyền. Đối tượng này bao gồm các thông tin như địa chỉ email,
    * mật khẩu và danh sách các quyền (GrantedAuthorities) mà người dùng có.
    * */

    /*
    * Set<GrantedAuthority> authorities = user.getRoles()...:
    * Đoạn này trích xuất các quyền (roles) của người dùng từ đối tượng user,
    * sau đó tạo một tập hợp (Set) các đối tượng GrantedAuthority từ các quyền này.
    * Mỗi quyền sẽ được chuyển đổi thành một đối tượng SimpleGrantedAuthority.
    * */
}
