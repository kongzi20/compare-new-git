package com.test.example.code.compare.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.test.example.code.compare.dao.FileDao;
import com.test.example.code.compare.model.ArchiveFile;
import com.test.example.code.compare.model.FileTypeEnum;
import com.test.example.core.exception.ServiceException;
import com.test.example.core.sf.ServiceFactory;
 

/**
 * 文件操作类.
 * 
 * 
 * @author liulijie
 */
@Service("fileService")
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {

	private static final long serialVersionUID = 8694387871488481464L;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileDao fileDao;

	@Resource(name = "serviceFactory")
	private ServiceFactory serviceFactory;

	@Value("${uploadFileRootDir}")
	private String uploadFileRootDir;

	// 拼装文件的绝对路径
	private String buildAbsoluteFilePath(String path) {
		String apath = uploadFileRootDir;
		if (!apath.endsWith(File.separator)) {
			apath += File.separator;
		}
		apath += path;
		return apath;
	}

	@Override
	public void removeFile(String dbkey) throws ServiceException {
		ArchiveFile afile = fileDao.removeFile(dbkey);
		if (null != afile) {
			afile.setFile(new File(buildAbsoluteFilePath(afile.getFilePath())));
			afile.getFile().delete();
		} else {
			logger.error("删除文件失败！");
			throw new ServiceException("删除文件失败！");
		}
	}

	@Override
	public String saveFile(ArchiveFile file, FileTypeEnum type) throws ServiceException {
		Assert.notNull(file, "file不允许为空！");
		Assert.notNull(type, "type不允许为空！");

		AppSettingService appService = serviceFactory.getService(AppSettingService.class);
		file.setFileType(file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1,
				file.getFileName().length()));
		String filePath = type.name() + File.separator + appService.getDate4Path();
		File saveFilePath = new File(buildAbsoluteFilePath(filePath));
		if (!saveFilePath.exists()) { // 创建保存文件的路径
			saveFilePath.mkdirs();
		}
		file.setFilePath(filePath);
		String fileCode = fileDao.saveFile(file, type);
		filePath = saveFilePath.getAbsolutePath() + File.separator + fileCode + "." + file.getFileType();
		OutputStream os = null;
		InputStream is = null;
		try {
			os = new FileOutputStream(filePath);
			is = new FileInputStream(file.getFile());
			byte[] tempbytes = new byte[16 * 1024];
			int byteread = 0;
			while ((byteread = is.read(tempbytes)) != -1) {
				os.write(tempbytes, 0, byteread);
			}
		} catch (Exception e) {
			logger.error("存储文件失败！", e);
			throw new ServiceException(e);
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				logger.error("存储文件失败！", e);
				e.printStackTrace();
			}
		}
		return fileCode;
	}

	/**
	 * 根据fileCode获取文件存储路径
	 * 
	 * @param fileCode
	 *            文件唯一标识码
	 * @return 返回的ArchiveFile对象，主要是文件保存路径和原文件名
	 * @throws ServiceException
	 */
	@Override
	public ArchiveFile getArchiveFile(String fileCode) throws ServiceException {
		ArchiveFile afile = fileDao.getArchiveFile(fileCode);
		if (null == afile) {
			String error = "找不到存储文件路径";
			logger.error("文件下载失败！", error);
			throw new ServiceException(error);
		}
		if (afile.getFilePath().indexOf(":") != -1) {
			afile.setFile(new File(afile.getFilePath()));
		} else {
			File  f = new File(buildAbsoluteFilePath(afile.getFilePath()));
			
			if(!f.exists())
				f = new File(afile.getFilePath());
				afile.setFile(f);
		}
		return afile;
	}

	@Override
	public ArchiveFile findById(String fileCode) {
		return fileDao.get(fileCode);
	}
}
