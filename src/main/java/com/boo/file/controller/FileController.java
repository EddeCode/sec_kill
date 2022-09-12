package com.boo.file.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.boo.common.ResponseResult;
import com.boo.file.entity.FileMap;
import com.boo.file.mapper.FileMapMapper;
import com.boo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author song
 * @date 2022/4/27 16:04
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {

    @Autowired
    UserService userService;
    @Value("${file-storage-directory}")
    String directoryPath;
    @Autowired
    MimeTypes mimeTypes;
    @Autowired
    FileMapMapper fmMapper;
    public static String sc = String.valueOf(File.separatorChar);

    @RequestMapping("/skuImg")
    @PreAuthorize("hasAnyAuthority('sys.mowner')")
    public ResponseResult<String> skuImg(@RequestPart("img") MultipartFile img) throws IOException, MimeTypeException {
        Long uid = userService.getUserInSecurityContext().getId();

        long imgId = Timestamp.from(Instant.now()).getTime();
        String fileSeq = +uid + "-" + imgId;

        String extension = mimeTypes.forName(img.getContentType()).getExtension();
        //需要新建文件夹 不然会报错
        String filePath =
                Path.of(directoryPath) + sc + "sku-img" + sc + fileSeq + extension;
        img.transferTo(new File(filePath));
        fmMapper.insert(new FileMap(uid, fileSeq, extension, true));
        return new ResponseResult<>(200, "success", fileSeq);
    }

    @GetMapping("/getImgList")
    @PreAuthorize("hasAnyAuthority('sys.mowner')")
    public ResponseResult<List<FileMap>> getImgList() {
        Long uid = userService.getUserInSecurityContext().getId();
        LambdaQueryWrapper<FileMap> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FileMap::getUid, uid).eq(FileMap::getStatus, true);
        return new ResponseResult<>(200, fmMapper.selectList(wrapper));
    }

    /**
     * 为什么没有生效 @PreAuthorize("permitAll()")
     * 注解和方法配置时两套认证系统，当方法配置无法靠近controller端点时，无法触发注解
     */
    @GetMapping("/skuImg/{fileName}")
    public ResponseEntity<byte[]> getSkuImg(@PathVariable String fileName) throws IOException {
        if (!StringUtils.hasText(fileName) || "null".equals(fileName) || "undefined".equals(fileName)) {
            return null;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        boolean exist = FileUtil.exist(directoryPath + sc + "sku-img" + sc + fileName);
        if (!exist) {
            return new ResponseEntity<>(new byte[0], httpHeaders, HttpStatus.OK);
        }
        byte[] bytes =
                Files.readAllBytes(Path.of(directoryPath + sc + "sku-img" + sc + fileName));
        return new ResponseEntity<>(bytes,
                httpHeaders, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('sys.mowner')")
    @PostMapping("/deleteImg")
    public ResponseResult deleteImg(@RequestBody FileMap fm) {
        LambdaUpdateWrapper<FileMap> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(FileMap::getStatus, false);
        updateWrapper.eq(FileMap::getFileSeq, fm.getFileSeq()).eq(FileMap::getExtension,
                fm.getExtension());
        fmMapper.update(null, updateWrapper);
        return new ResponseResult(200, "ok");
    }
}
