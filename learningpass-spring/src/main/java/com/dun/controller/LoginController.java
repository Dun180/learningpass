package com.dun.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dun.common.dto.LoginDto;
import com.dun.common.lang.Result;
import com.dun.entity.User;
import com.dun.service.UserService;
import com.dun.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        User user = userService.getUserByUsername(loginDto.getUsername());
        if (user == null){
            return Result.fail("用户不存在");
        }

//        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
//            return Result.fail("密码不正确");
//        }

        if (!user.getPassword().equals(loginDto.getPassword())){
            return Result.fail("密码不正确");
        }

        String jwt = jwtUtils.generateToken(user.getId());

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("username",user.getUsername())
                .map()
        );
    }

    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request){
        Boolean checkResult;
        String token = request.getHeader("token");
        //校验jwt
        Claims claim = jwtUtils.getClaimByToken(token);
        if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())){
            checkResult = false;
        }else {
            checkResult = true;
        }

        return Result.succ(MapUtil.builder()
                .put("checkResult",checkResult)
                .map()
        );
    }
}
