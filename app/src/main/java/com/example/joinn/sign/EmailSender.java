package com.example.joinn.sign;
import android.os.AsyncTask;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        final String toEmail = params[0];
        final String verificationCode = params[1];

        final String username = "dldnwls0115@hs.ac.kr";
        final String password = "qlalf0115!";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("이메일 인증코드");
            message.setText("사용자의 이메일 인증 코드6자리: " + verificationCode);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // 이 메서드는 백그라운드 작업이 완료된 후에 호출됩니다.
        // 여기에서 UI 업데이트 또는 다른 작업을 수행할 수 있습니다.
    }
}
