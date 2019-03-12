package com.wether.demo.mail;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Zhangchen on 2016/11/17.
 */
public class MailBean {
	private String toEmailList;
	private String ccEmailList;
	private String bccEmailList;
	private String emailSender;
	private String displayName;
	private String subject;
	private String content;
	private List<String> filePathList;
	private List<String> fileNameList;
	private List<InputStream> fileStreamList;
	private File[] attachements;
	private String seperator;

	public String getToEmailList() {
		return toEmailList;
	}

	public void setToEmailList(String toEmailList) {
		this.toEmailList = toEmailList;
	}

	public String getCcEmailList() {
		return ccEmailList;
	}

	public void setCcEmailList(String ccEmailList) {
		this.ccEmailList = ccEmailList;
	}

	public String getBccEmailList() {
		return bccEmailList;
	}

	public void setBccEmailList(String bccEmailList) {
		this.bccEmailList = bccEmailList;
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

	public List<String> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" toEmailList=").append(this.toEmailList);
		buffer.append(" ,ccEmailList=").append(this.ccEmailList);
		buffer.append(" ,bccEmailList=").append(this.bccEmailList);
		buffer.append(" ,emailSender=").append(this.emailSender);
		buffer.append(" ,displayName=").append(this.displayName);
		buffer.append(" ,subject=").append(this.subject);
		buffer.append(" ,content=").append(this.content);
		return buffer.toString();
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public File[] getAttachements() {
		return attachements;
	}

	public void setAttachements(File[] attachements) {
		this.attachements = attachements;
	}


	public List<InputStream> getFileStreamList() {
		return fileStreamList;
	}

	public void setFileStreamList(List<InputStream> fileStreamList) {
		this.fileStreamList = fileStreamList;
	}

}
