package com.business.tools;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EMailUtil implements Serializable{
	private static final long serialVersionUID = -489464677911126287L;
	
	private Properties props;
	private Session session;
	private Message msg;
	private Calendar sendTime;
	private String title;
	private String content;
	
	private static Logger logger = LoggerFactory.getLogger(EMailUtil.class);
	
	public EMailUtil(){
		super();
		this.props = new Properties();
		this.props.setProperty("mail.transport.protocol", "smtp"); //协议
		this.props.setProperty("mail.smtp.host", "smtp.exmail.qq.com"); //主机名
		this.props.setProperty("mail.smtp.auth", "true"); //是否开启权限控制
		this.props.setProperty("mail.debug", "true"); //返回发送的cmd源码
		this.session = Session.getInstance(props);
		this.msg = new MimeMessage(session);
	} 
	
	private void doSendMsg(String title,String content) throws Exception{
		msg.setFrom(new InternetAddress("wubin@cootoo.com")); //自己的email
		msg.setRecipient(RecipientType.TO, new InternetAddress("45672713@qq.com")); // 要发送的email，可以设置数组
		msg.setSubject(title);  //邮件标题
		msg.setText(content); //邮件正文
		//不被当作垃圾邮件的关键代码--Begin ，如果不加这些代码，发送的邮件会自动进入对方的垃圾邮件列表
		msg.addHeader("X-Priority", "3"); 
		msg.addHeader("X-MSMail-Priority", "Normal"); 
		msg.addHeader("X-Mailer", "Microsoft Outlook Express 6.00.2900.2869"); //本文以outlook名义发送邮件，不会被当作垃圾邮件 
		msg.addHeader("X-MimeOLE", "Produced By Microsoft MimeOLE V6.00.2900.2869"); 
		msg.addHeader("ReturnReceipt", "1"); 
		//不被当作垃圾邮件的关键代码--end
		Transport trans = session.getTransport(); 
		trans.connect("wubin@cootoo.com", "Wubin303052"); // 邮件的账号密码
		trans.sendMessage(msg, msg.getAllRecipients());
	}
	
	
	public void SendEMailMsg(){
		Calendar nowTime = Calendar.getInstance();
		if(this.sendTime == null){
			try{
				doSendMsg(this.title,this.content);
			}catch(Exception e){
				e.printStackTrace();
				logger.debug("发送邮件异常:异常结果为"+e.toString());
			}finally {
				this.sendTime = nowTime;
			}
		}
		else if((nowTime.getTimeInMillis() - sendTime.getTimeInMillis())/7200000 > 1){
			try{
				doSendMsg(this.title,this.content);
			}catch(Exception e){
				e.printStackTrace();
				logger.debug("发送邮件异常:异常结果为"+e.toString());
			}finally {
				this.sendTime = nowTime;
			}
		}else{
			logger.info("发送邮件失败:两小时内有发送过邮件，上次发送时间为:"+sendTime+" 这次发送时间为: "+nowTime);
		}
	}

	public Calendar getSendTime() {
		return sendTime;
	}

	public void setSendTime(Calendar sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
