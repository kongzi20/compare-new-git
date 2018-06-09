/**
 *  Licensed to test-System co.
 */
package com.test.example.code.compare.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.test.example.code.compare.model.ArchiveFile;
import com.test.example.code.compare.model.FileTypeEnum;
import com.test.example.core.dao.hibernate.SimpleHibernateDao;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.utils.DateUtils;

 

/**
 * 
 * @author liulijie
 */
@Repository
public class FileDao extends SimpleHibernateDao<ArchiveFile, String> {

	/**
	 * 根据id删除文件.
	 * 
	 * @param fileCode
	 * @return 主要用于获取文件路径并删除文件系统中的文件
	 */
	public ArchiveFile removeFile(String fileCode) throws ServiceException {
		ArchiveFile afile = null;
		if (StringUtils.isBlank(fileCode)) {
			return afile;
		}

		afile = getArchiveFile(fileCode);

		List<Object> params = new ArrayList<Object>();
		params.add(fileCode);
		createSqlQuery("DELETE FROM ARCHIVE_FILES WHERE FILE_CODE = ?", params).executeUpdate();
		return afile;
	}

	/**
	 * 保存文件信息到数据库中.
	 * 
	 * @param file
	 * @param type
	 * @return 返回的String为uuid保证生产文件名唯一
	 * @throws ServiceException
	 */
	public String saveFile(ArchiveFile file, FileTypeEnum type) throws ServiceException {
		if (null == file || null == type) {
			return null;
		}
		UUID uuid = UUID.randomUUID();
		String fileCode = uuid.toString();
		fileCode = fileCode.replaceAll("-", "");
		StringBuffer sql = new StringBuffer(
				"INSERT INTO ARCHIVE_FILES(FILE_CODE,FILE_TYPE,FILE_PATH,CREATE_TIME,DESCRIPTION,FILE_NAME) ");
		sql.append("VALUES(?,?,?,SYSDATE,?,?)");
		List<Object> params = new ArrayList<Object>();
		params.add(fileCode);
		params.add(type.name());
		params.add(file.getFilePath() + fileCode
				+ file.getFileName().substring(file.getFileName().lastIndexOf("."), file.getFileName().length()));
		params.add(file.getDesc());
		params.add(file.getFileName());
		createSqlQuery(sql.toString(), params).executeUpdate();
		return String.valueOf(fileCode);
	}

	/**
	 * 根据fileCode获取文件存储路径
	 * 
	 * @param fileCode
	 *            文件唯一标识码
	 * @return 返回的ArchiveFile对象，主要是文件保存路径和原文件名
	 * @throws ServiceException
	 */
	public ArchiveFile getArchiveFile(String fileCode) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		params.add(fileCode);
		@SuppressWarnings("rawtypes")
		List list = createSqlQuery("SELECT FILE_PATH, FILE_NAME, TO_CHAR(CREATE_TIME, 'yyyy-MM-dd HH24:mi:ss') FROM ARCHIVE_FILES WHERE FILE_CODE= ?", params).list();
		if (null == list || list.isEmpty()) {
			return null;
		}
		Object[] obj = (Object[]) list.get(0);
		ArchiveFile afile = new ArchiveFile();
		afile.setId(fileCode);
		afile.setFilePath(obj[0].toString());
		afile.setFileName(obj[1].toString());
		afile.setUploadDate(DateUtils.toDate(obj[2].toString()));
		return afile;
	}
}
