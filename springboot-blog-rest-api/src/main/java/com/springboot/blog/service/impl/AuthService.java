package com.springboot.blog.service.impl;

import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
