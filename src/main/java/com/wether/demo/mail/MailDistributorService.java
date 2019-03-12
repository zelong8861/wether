package com.wether.demo.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by Zhangchen on 2016/11/17.
 */

public class MailDistributorService {

	private static MailDistributorService ourInstance;

	public static synchronized MailDistributorService getInstance() {
		if (ourInstance == null) {
			ourInstance = new MailDistributorService();
		}

		return ourInstance;
	}

	private MailDistributorService() {
		init();
	}

	private String mailHost;
	private String userName;
	private String password;
	private String emailSender;
	private String displayName;

	public void send(MailBean mail) throws Exception {
		if (mail == null) {
			return;
		}
		InternetAddress[] mailRecipients = null;
		InternetAddress[] mailCcs = null;
		InternetAddress[] mailBccs = null;

		String seperator = mail.getSeperator();
		if (seperator == null)
			mail.setSeperator(";");
		mailRecipients = getRecipients(mail.getToEmailList(), mail.getSeperator());
		mailCcs = getRecipients(mail.getCcEmailList(), mail.getSeperator());
		mailBccs = getRecipients(mail.getBccEmailList(), mail.getSeperator());

		if (mailHost == null) {
			throw new Exception("Cannot get SMTP server!");
		}
		if (mail.getEmailSender() == null && emailSender == null) {
			throw new Exception("Cannot get Email sender!");
		}
		/*
		 * if (mailRecipients == null) { throw new Exception(
		 * "Cannot get Email receiver!"); }
		 */
		Properties mailProp = new Properties();
		mailProp.put("mail.smtp.host", mailHost);
		mailProp.put("mail.smtp.from", mail.getEmailSender() == null ? emailSender : mail.getEmailSender());

		// mailProp.put("mail.smtp.auth", "true");

		Session mailSession = Session.getInstance(mailProp);
		Transport transport = mailSession.getTransport("smtp");
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(mail.getEmailSender() == null ? emailSender : mail.getEmailSender(),
				mail.getDisplayName() == null ? displayName : mail.getDisplayName()));
		message.setRecipients(Message.RecipientType.TO, mailRecipients);
		if (mailCcs != null && mailCcs.length > 0) {
			message.setRecipients(Message.RecipientType.CC, mailCcs);
		}
		if (mailBccs != null && mailBccs.length > 0) {
			message.setRecipients(Message.RecipientType.BCC, mailBccs);
		}
		message.setSubject(mail.getSubject());

		MimeMultipart multi = new MimeMultipart();
		BodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setContent(mail.getContent(), "text/html;charset=GBK");
		multi.addBodyPart(textBodyPart);
	
		if ((mail.getFilePathList() != null) && (mail.getFilePathList().size() > 0) && mail.getFileNameList() != null
				&& (mail.getFileNameList().size() > 0)
				&& mail.getFilePathList().size() == mail.getFileNameList().size()) {
			for (int i = 0; i < mail.getFilePathList().size(); i++) {
				MimeBodyPart fileBodyPart = new MimeBodyPart();
				String filePath = mail.getFilePathList().get(i);
				String fileName = mail.getFileNameList().get(i);
				if (filePath != null) {
					FileDataSource fds = new FileDataSource(filePath);
					fileBodyPart.setDataHandler(new DataHandler(fds));
					//fileBodyPart.setFileName(fileName);
					fileBodyPart.setFileName(new String(fileName.getBytes("GBK"), "ISO-8859-1"));
					multi.addBodyPart(fileBodyPart);
				}
			}
		}
		if ((mail.getFileStreamList() != null) && (mail.getFileStreamList().size() > 0) && mail.getFileNameList() != null && (mail.getFileNameList().size() > 0) && mail.getFileStreamList().size() == mail.getFileNameList().size()) {
			for (int i = 0; i < mail.getFileStreamList().size(); i++) {
				MimeBodyPart fileBodyPart = new MimeBodyPart();
				InputStream fileStream = mail.getFileStreamList().get(i);
				String fileName = mail.getFileNameList().get(i);
				if (fileStream != null) {
					DataSource fds = new ByteArrayDataSource(fileStream,"application/octet-stream");
					fileBodyPart.setDataHandler(new DataHandler(fds));
					//fileBodyPart.setFileName(MimeUtility.encodeText(fileName));
					fileBodyPart.setFileName(new String(fileName.getBytes("GBK"), "ISO-8859-1"));
					multi.addBodyPart(fileBodyPart);
				}
			}
		}


		if (mail.getAttachements() != null && mail.getAttachements().length >= 1) {
			for (File file : mail.getAttachements()) {
				if (file != null) {
					MimeBodyPart fileBodyPart = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(file);
					fileBodyPart.setDataHandler(new DataHandler(fds));
					//fileBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
					fileBodyPart.setFileName(new String(file.getName().getBytes("GBK"), "ISO-8859-1"));
					multi.addBodyPart(fileBodyPart);
				}
			}
		}


		message.setContent(multi);
		message.saveChanges();

		if (userName != null && !userName.equals("") && password != null && !password.equals("")) {
			transport.connect(mailHost, userName, password);
			transport.sendMessage(message, message.getAllRecipients());

			// if (mailRecipients != null && mailRecipients.length > 0)
			// transport.sendMessage(message, mailRecipients);
			// if (mailCcs != null && mailCcs.length > 0)
			// transport.sendMessage(message, mailCcs);
			// if (mailBccs != null && mailBccs.length > 0)
			// transport.sendMessage(message, mailBccs);

			transport.close();
		} else
			Transport.send(message);
		System.out.println("Send email successfully!");
	}

	private static InternetAddress[] getRecipients(String strAddress) throws Exception {
		/*
		 * if (strAddress == null) return null;
		 * 
		 * Vector<String> addressVec = new Vector<String>(); String address =
		 * null; StringTokenizer st = new StringTokenizer(strAddress, ";");
		 * while (st.hasMoreElements()) { address = st.nextElement().toString();
		 * addressVec.addElement(address); }
		 * 
		 * int size = addressVec.size(); InternetAddress[] inetAddress = new
		 * InternetAddress[size]; for (int i = 0; i < size; i++) {
		 * inetAddress[i] = new
		 * InternetAddress(addressVec.elementAt(i).toString()); }
		 * 
		 * return inetAddress;
		 */
		return getRecipients(strAddress, ";");
	}

	private static InternetAddress[] getRecipients(String strAddress, String seperator) throws Exception {
		if (strAddress == null)
			return null;

		Vector<String> addressVec = new Vector<String>();
		String address = null;
		StringTokenizer st = new StringTokenizer(strAddress, seperator);
		while (st.hasMoreElements()) {
			address = st.nextElement().toString();
			addressVec.addElement(address);
		}

		int size = addressVec.size();
		InternetAddress[] inetAddress = new InternetAddress[size];
		for (int i = 0; i < size; i++) {
			inetAddress[i] = new InternetAddress(addressVec.elementAt(i).toString());
		}

		return inetAddress;
	}

	private void init() {
		mailHost = MailProp.getProperty("emailSetting.mailHost");
		userName = MailProp.getProperty("emailSetting.userName");
		password = MailProp.getProperty("emailSetting.password");
		emailSender = MailProp.getProperty("emailSetting.emailSender");
		displayName = MailProp.getProperty("emailSetting.displayName");
	}
}
