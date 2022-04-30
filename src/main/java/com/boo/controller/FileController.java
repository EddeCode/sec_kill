package com.boo.controller;

import com.boo.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author song
 * @date 2022/4/27 16:04
 */
@Controller
@Slf4j
public class FileController {

    @Autowired
    UserService userService;

    // @RequestMapping("/user/getAvatar/{uid}")
    // public ResponseEntity<byte[]> getAvatar(@PathVariable Long uid) throws SQLException, IOException {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Content-Disposition", "attachment;filename=" + uid + ".jpg");
    //     ResponseEntity<byte[]> responseEntity =
    //             new ResponseEntity<>(userService.getAvatar(uid), headers,
    //                     HttpStatus.OK);
    //     log.info("headers:{}",headers);
    //     return responseEntity;
    // }
}
