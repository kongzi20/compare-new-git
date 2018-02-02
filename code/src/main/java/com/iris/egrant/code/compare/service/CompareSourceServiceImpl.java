package com.iris.egrant.code.compare.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iris.egrant.code.compare.dao.CompareDao;
import com.iris.egrant.code.compare.dao.CompareSourceDao;
import com.iris.egrant.code.compare.model.ArchiveFile;
import com.iris.egrant.code.compare.model.CompareSource;
import com.iris.egrant.code.compare.model.CompareTemplateSetting;
import com.iris.egrant.core.exception.DaoException;
import com.iris.egrant.core.exception.ServiceException;
import com.iris.egrant.core.sf.ServiceFactory;
import com.iris.egrant.core.utils.ReadFileUtils;


/**
 * 对比数据来源相关service
 * 
 * @author leijiang
 * 
 */
@Service("compareSourceService")
@Transactional(rollbackFor = Exception.class)
public class CompareSourceServiceImpl implements CompareSourceService {

	/**
	 *
	 */
	private static final long serialVersionUID = -6042881317298490225L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CompareSourceDao compareSourceDao;
	@Autowired
	private CompareDao compareDao;
	
	@Autowired
	private FileService fileService;

	@Value("${uploadFileRootDir}")
	private String uploadFileRootDir;
	
	@Resource(name = "serviceFactory")
	private ServiceFactory serviceFactory;

	@Override
	public void mergeContent(CompareSource cs) throws ServiceException {
		if (cs == null) {
			return;
		}

		try {
			// 获取文件或xml最终内容
			String content = getContent(cs);

			cs.setContent(content);

			cs.setStatus(1);
			cs.setErrMsg("");
			// 调用插入至compare_list和comopare_result表的存储过程
			compareSourceDao.callExec(cs.getKeyCode(), cs.getType(), content);

		} catch (Exception e) {
			cs.setStatus(2);
			cs.setErrMsg(e.getMessage());
		} finally {
			cs.setCompleteDate(new Date());
			updateCompareSource(cs);
		}

	}

	@Override
	public void callExec(Long keyCode, Integer type, String content) throws ServiceException {

		try {
			compareSourceDao.callExec(keyCode, type, content);
		} catch (DaoException e) {

			throw new ServiceException("执行存储过程错误,errMsg=" + e.getMessage());
		}

	}

	/**
	 * wk add 2014-5-12
	 * 根据dataType初始化表compare_list和表compare_result数据
	 * 
	 * @param cs
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public void callExecByDataType(Long keyCode, Integer type,
			Integer dataType, String content) throws ServiceException {
		try {
			compareSourceDao.callExecByDataType(keyCode, type, dataType, content);
		} catch (DaoException e) {

			throw new ServiceException("执行存储过程错误,errMsg=" + e.getMessage());
		}
		
	}

	@Override
	public String getContent(CompareSource cs) throws ServiceException {
		String content = "";
		if (cs == null) {
			return content;
		}
		try {
			List<Map<String, Object>> list = compareSourceDao.getContentSettingList(cs.getType(), cs.getFormCode());
			// System.out.println("22");
			for (Map<String, Object> map : list) {
				int dataType = Integer.parseInt(map.get("DATA_TYPE").toString());
				String xpath = map.get("XPATH").toString();
				String tempContent = "";
				// 获取filecode的相对路径
				String path = xpath.substring(xpath.lastIndexOf("/"), xpath.lastIndexOf("[") == -1 ? xpath.length()
						: xpath.lastIndexOf("["));
				path += "/file_code";
				tempContent = compareSourceDao.getDBContent(cs.getKeyCode(), cs.getType(), dataType, xpath, path);
				if (StringUtils.isBlank(tempContent)) {
					continue;
				}

				if (dataType == 1) {// xpath的节点值为大文本
					content += tempContent + "\r\n";
				} else if (dataType == 2) {// xpath的节点值为fileCode
					String[] fileCodeAry = tempContent.split(",");
					for (String fileCode : fileCodeAry) {
						if (StringUtils.isNotBlank(fileCode)) {
							content += getFileContent(fileCode) + "\r\n";
						}

					}
				} else {
					content += tempContent + "\r\n";
				}
			}
		} catch (Exception e) {
			logger.error("获取相似度数据源内容出错,keyCode=" + cs.getKeyCode() + ",type=" + cs.getType() + ",errMsg="
					+ e.getMessage());
			throw new ServiceException("获取相似度数据源内容出错,keyCode=" + cs.getKeyCode() + ",type=" + cs.getType() + ",errMsg="
					+ e.getMessage());
		}

		return content;
	}
	
	/**
	 * wk add 2014-5-12
	 * 根据dataType抽取待比较内容
	 */
	@Override
	public String getContentByDataType(CompareSource cs) throws ServiceException {

		// 获取文件或xml最终内容
		String content = "";
		if (cs == null) {
			return content;
		}
		try {
			// 1.通过dataType获取对应实现类
			CompareTemplateSetting cts = compareDao.getCompareBeanNameByDataType(cs.getDataType());
			CompareTemplateService ctService = this.getServiceFactory().getService(cts.getTemplateBeanName(), CompareTemplateService.class);
	
			// 2.调用实现类抽取方法抽取文本
			content = ctService.extractCompareSource(cs.getKeyCode());
		} catch (Exception e) {
			logger.error("获取相似度数据源内容出错,keyCode=" + cs.getKeyCode() + ",type=" + cs.getType() + ",errMsg="
					+ e.getMessage());
			throw new ServiceException("获取相似度数据源内容出错,keyCode=" + cs.getKeyCode() + ",type=" + cs.getType() + ",errMsg="
					+ e.getMessage());
		}
		
		return content;
		
	}

	@Override
	public List<CompareSource> getCompareSourceList(int startId, int fetchSize) throws ServiceException {
		return compareSourceDao.getCompareSourceList(startId, fetchSize);
	}

	@Override
	public List getKeyCode(int startId, int fetchSize) throws ServiceException {
		return compareSourceDao.getKeyCode(startId, fetchSize);
	}

	/**
	 * 获取文件内容
	 * 
	 * @param fileCode
	 * @return
	 * @throws ServiceException
	 */
	private String getFileContent(String fileCode) throws ServiceException {
		String content = "";
		if (StringUtils.isBlank(fileCode)) {
			return content;
		}

		try {
			ArchiveFile af = fileService.getArchiveFile(fileCode);
			if (af != null) {
				String filePath = af.getFilePath();
				// 获取文件的绝对路径
				filePath = buildAbsoluteFilePath(filePath);
				if (filePath.endsWith(".doc")) {
					content = ReadFileUtils.readWord2003(filePath);
				} else if (filePath.endsWith(".docx")) {
					content = ReadFileUtils.readWord2007(filePath);
				} else if (filePath.endsWith(".pdf")) {
					content = ReadFileUtils.readPdf(filePath);
				}
			}
		} catch (Exception e) {
			logger.error("相似度对比中获取附件内容出错,fileCode=" + fileCode + ",errMsg=" + e.getMessage());
			throw new ServiceException("相似度对比中获取附件内容出错,fileCode=" + fileCode + ",errMsg=" + e.getMessage());
		}
		return content;
	}

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
	public void updateCompareSource(CompareSource cs) throws ServiceException {
		try {
			compareSourceDao.updateCompareSource(cs);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}
	
	@Override
	public String getDBContent(long prpCode, int type, int dataType, String path,String path2) {
		return compareSourceDao.getDBContent(prpCode, type, dataType, path, path2);
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getSpecialCompareNodeByGrantCode(Long grantCode,Integer type,Integer dataType) {
		String sql="select t.* from grant_compare_node t where t.grant_code=? and t.type=? and t.data_type=?";
		return compareSourceDao.queryForList(sql, new Object[]{grantCode,type,dataType});
	}

	
	
}