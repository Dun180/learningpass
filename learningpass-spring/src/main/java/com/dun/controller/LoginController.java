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
import java.util.Date;
import java.util.Map;

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

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
            return Result.fail("密码不正确");
        }

        //jwt生成
        String jwt = jwtUtils.generateToken(user.getId());

        response.setHeader("Authorization",jwt);
        response.setHeader("Access-control-Expose-Headers","Authorization");

        return Result.succ(MapUtil.builder()
                .put("id",user.getId())
                .put("name",user.getName())
                .put("identity",user.getIdentity())
                .map()
        );
    }

    @PostMapping("/register")
    public Result register(@RequestBody Map<String,Object> map){
        System.out.println(map);
        User user = new User();
        user.setUsername(map.get("username").toString());
        user.setName("用户"+map.get("username").toString());
        user.setPassword(SecureUtil.md5(map.get("password").toString()));
        user.setIdentity(map.get("identity").toString());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        if (userService.register(user)){
            return Result.succ(MapUtil.builder()
                    .put("registerResult",true)
                    .map()
            );
        }else {
            return Result.fail("注册失败");
        }

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
