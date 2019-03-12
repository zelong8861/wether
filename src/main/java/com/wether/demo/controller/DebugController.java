package com.wether.demo.controller;


import com.wether.demo.mail.MailBean;
import com.wether.demo.mail.MailDistributorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        MailDistributorService mailDistributorService = MailDistributorService.getInstance();
        MailBean mailBean = new MailBean();
        mailBean.setToEmailList("zelong.li@cicc.com.cn");
        mailBean.setSubject("test");
        mailBean.setContent("test");

        try {
            mailDistributorService.send(mailBean);
            return "hello spring!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


