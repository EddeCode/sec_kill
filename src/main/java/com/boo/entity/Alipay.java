package com.boo.entity;
 
 
 
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
 
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class Alipay {
 
    /**日志对象*/
    private static final Logger logger = LoggerFactory.getLogger(Alipay.class);
 
    private final String format = "json";
 
    /**
     * appId
     */
    private String appId;
 
    /**
     * 商户私钥
     */
    private String privateKey;
 
    /**
     * 支付宝公钥
     */
    private String publicKey;
 
    /**
     * 服务器异步通知页面路径，需要公网能访问到
     */
    @Value("http://localhost:8080/ssm/error_url.html")
    private String notifyUrl;
 
    /**
     * 服务器同步通知页面路径，填写自己的成功页面路径
     */
    @Value("http://localhost:8080/ssm/success_url.html")
    private String returnUrl;
 
    /**
     * 签名方式
     */
    @Value("RSA2")
    private String signType;
 
    /**
     * 字符编码格式
     */
    @Value("utf-8")
    private String charset;
 
    /**
     * 支付宝网关
     */
    private String gatewayUrl;
 
    public String pay(AliPayBean aliPayBean) throws AlipayApiException {
 
        AlipayClient alipayClient = new DefaultAlipayClient(
                gatewayUrl, appId, privateKey, format, charset, publicKey, signType);
 
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setBizContent(JSON.toJSONString(aliPayBean));
        logger.info("封装请求支付宝付款参数为:{}", JSON.toJSONString(alipayRequest));
 
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        logger.info("请求支付宝付款返回参数为:{}", result);
 
        return result;
    }
}
 
 