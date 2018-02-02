package com.iris.egrant.code.compare.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

/**
 * 附件model.
 * 
 * 
 * 
 */
public class ArchiveFile implements Serializable {

	private static final long serialVersionUID = 8742706628819332275L;

	private String id;

	private File file;
	private String fileName;
	private Long length;
	private Date uploadDate;
	private String fileType;
	private String filePath;
	// 0:未删除 1:删除
	private int status = 0;
	private Long uploadUserId;
	private String desc;
    private Long pdfAttachmentId;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Transient
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getPdfAttachmentId() {
		return pdfAttachmentId;
	}

	public void setPdfAttachmentId(Long pdfAttachmentId) {
		this.pdfAttachmentId = pdfAttachmentId;
	}

}
