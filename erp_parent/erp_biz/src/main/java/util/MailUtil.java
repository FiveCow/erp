package util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件工具
 * @author Administrator
 *
 */
public class MailUtil {

	private String fromAddress;//发件人
		
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	private JavaMailSender javaMailSender;
	
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}


	/**
	 * 发送邮件
	 * @param toAddress 收件人
	 * @param subject 主题
	 * @param text 正文
	 * @throws MessagingException 
	 */
	public void sendMail(String toAddress,String subject,String text) throws MessagingException{
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
		helper.setFrom(fromAddress);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(text);
		javaMailSender.send(mimeMessage);
		
	}
	
}
