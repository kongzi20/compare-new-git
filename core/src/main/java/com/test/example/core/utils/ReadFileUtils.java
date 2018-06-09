package com.test.example.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * 读取文件转换为文本工具类
 * 
 * @author leijiang
 * 
 */
public class ReadFileUtils {
	/**
	 * 读取word 2003文件转换为String
	 * 
	 * @param path
	 * @return
	 */
	public static String readWord2003(String path) throws Exception {
		// word 2003： 图片不会被读取
		FileInputStream fis = new FileInputStream(path);
		WordExtractor wordExtractor = new WordExtractor(fis);
		return wordExtractor.getText();
	}

	/**
	 * 读取word 2007文件转换为String
	 * 
	 * @param path
	 * @return
	 */
	public static String readWord2007(String path) throws Exception {
		// word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
		File file = new File(path);
		if (file.length() == 0) {
			return "";
		}
		OPCPackage oPCPackage = POIXMLDocument.openPackage(path);

		XWPFDocument xwpf = new XWPFDocument(oPCPackage);
		POIXMLTextExtractor ex = new XWPFWordExtractor(xwpf);
		return ex.getText();

	}

	/**
	 * 读取pdf文件转换为String
	 * 
	 * @param path
	 * @return
	 */
	public static String readPdf(String path) throws Exception {
		StringBuffer content = new StringBuffer();// 文档内容
		FileInputStream fis = new FileInputStream(path);
		PDFParser p = new PDFParser(fis);
		p.parse();
		PDFTextStripper ts = new PDFTextStripper();
		content.append(ts.getText(p.getPDDocument()));
		fis.close();
		return content.toString().trim();
	}

	/**
	 * 读取txt文件转换为String
	 * 
	 * @param path
	 * @return
	 */
	public static String readTxt(String path) {
		StringBuffer content = new StringBuffer("");// 文档内容
		try {
			FileReader reader = new FileReader(path);
			BufferedReader br = new BufferedReader(reader);
			String s1 = null;

			while ((s1 = br.readLine()) != null) {
				content.append(s1 + "/r");
			}
			br.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString().trim();
	}

	/**
	 * 读取html文件转换为String
	 * 
	 * @param urlString
	 * @return
	 */
	public static String readHtml(String urlString) {

		StringBuffer content = new StringBuffer("");
		File file = new File(urlString);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			// 读取页面
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"));// 这里的字符编码要注意，要对上html头文件的一致，否则会出乱码

			String line = null;

			while ((line = reader.readLine()) != null) {
				content.append(line + "/n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String contentString = content.toString();
		return contentString;
	}

	public static void main(String args[]) throws Exception {
		// String word2003Path="D:\\testWork\\01-Document\\example-product\\08-tools\\testGrid操作说明.doc";
		// // System.out.println(ReadFileUtils.readWord2003(word2003Path));

		// String word2007Path="D:\\testWork\\01-Document\\example-product\\08-tools\\公共组件使用说明.docx";
		// // System.out.println(ReadFileUtils.readWord2007(word2007Path));

		String pdfPath = "/home/test/下载/test.pdf";
		// System.out.println(ReadFileUtils.readPdf(pdfPath));

	}

}
