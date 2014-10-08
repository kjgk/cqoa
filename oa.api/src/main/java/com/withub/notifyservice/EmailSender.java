package com.withub.notifyservice;


public final class EmailSender implements NotifyMessageSender {

    public NotifyMessageSendResult sendMessage(NotifyMessageInfo notifyMessageInfo) {

        /*System.out.println("开始发送邮件......");

        NotifyMessageSendResult notifyMessageSendResult = new NotifyMessageSendResult();

        SmtpConfigInfo smtpConfigInfo = ConfigUtil.getSmtpConfigInfo();
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setSubject(notifyMessageInfo.getTitle());
            mail.setText(notifyMessageInfo.getContent());
            mail.setFrom(smtpConfigInfo.getMailAddress());
            mail.setTo(notifyMessageInfo.getAddress());

            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtpConfigInfo.getHost());
            properties.put("mail.smtp.port", smtpConfigInfo.getPort());
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.timeout", "25000");

            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            senderImpl.setUsername(smtpConfigInfo.getUsername());
            senderImpl.setPassword(smtpConfigInfo.getPassword());
            senderImpl.setJavaMailProperties(properties);
            senderImpl.send(mail);
            notifyMessageSendResult.setResultCode(1);
        } catch (Exception e) {
            e.printStackTrace();
            notifyMessageSendResult.setMessage(e.getMessage());
            notifyMessageSendResult.setResultCode(0);
        }

        return notifyMessageSendResult;*/
        return null;
    }
}
