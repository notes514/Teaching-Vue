package com.guidian.teaching.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description 此类控制器用于提供生成验证码
 * @author dhxstart
 * @date 2021/6/12 15:53
 */
@RestController
public class AuthController extends BaseController {
    @Autowired
    Producer producer;

    /**
     * @Description 生成图片验证码
     * @author dhxstart
     * @date 2021/6/12 16:13
     * @return com.guidian.teaching.common.lang.BaseResult
     * @throws IOException IO异常
     */
    @GetMapping("/captcha")
    public BaseResult captcha() throws IOException {
        String token = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64ImgStr = str + encoder.encode(outputStream.toByteArray());

        // 存储到redis中
        redisUtils.hset(Const.CAPTCHA_KEY, token, code, 120);
        return BaseResult.success(
                MapUtil.builder()
                        .put("token", token)
                        .put("captchaImg", base64ImgStr)
                        .build());
    }
}