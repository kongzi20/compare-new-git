package com.iris.egrant.code.compare.service;

import java.io.Serializable;

import com.iris.egrant.code.compare.model.ArchiveFile;
import com.iris.egrant.code.compare.model.FileTypeEnum;
import com.iris.egrant.core.exception.ServiceException;

  
/**
 * 文件操作接口.
 * 
 * 
 * @author liulijie
 */
public interface FileService extends Serializable {

	/**
	 * 根据id删除文件.
	 * 
	 * @param dbkey
	 */
	public void removeFile(String fileCode) throws ServiceException;

	/**
	 * 处理保存文件.
	 * 
	 * @param file
	 * @param type
	 * @return 返回的String为mongodbid-dbname
	 * @throws ServiceException
	 */
	public String saveFile(ArchiveFile file, FileTypeEnum type) throws ServiceException;

	/**
	 * 根据fileCode获取文件存储路径
	 * 
	 * @param fileCode
	 *            文件唯一标识码
	 * @return 返回的ArchiveFile对象，主要是文件保存路径和原文件名
	 * @throws ServiceException
	 */
	public ArchiveFile getArchiveFile(String fileCode) throws ServiceException;
	
	public ArchiveFile findById(String fileCode);

}
