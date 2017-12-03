package org.apache.http.spring.boot.client.utils;

import java.io.File;
import java.util.Properties;

import org.springframework.util.StringUtils;

/**
 * 
 * @className	： FilemimeUtils
 * @description	： 文件后缀处理工具以及对于相应头获取
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:19:22
 * @version 	V1.0
 */
public abstract class FilemimeUtils {

	protected static final String MIMETYPES_PROPERTIES = "mimeTypes.properties";
	protected static final String DEFAULT_MIME = "application/octet-stream";
	protected static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.load(FilemimeUtils.class
					.getResourceAsStream(MIMETYPES_PROPERTIES));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getFileMimeType(File file) {
		if (file == null) {
			return DEFAULT_MIME;
		}
		return getFileMimeType(file.getName());
	}

	public static String getFileMimeType(String fileName) {
		if ((StringUtils.isEmpty(fileName)) || (fileName.indexOf(".") == -1)) {
			return DEFAULT_MIME;
		}
		fileName = fileName.substring(fileName.lastIndexOf("."));
		return getExtensionMimeType(fileName);
	}

	public static String getExtensionMimeType(String extension) {
		String result = null;
		if (StringUtils.isEmpty(extension)) {
			return result;
		}
		extension = extension.toLowerCase();
		if (!(extension.startsWith("."))) {
			extension = "." + extension;
		}
		result = (String) properties.getProperty(extension, DEFAULT_MIME);
		return result;
	}

	public static void main(String[] args) {
		System.out.println("FileMimeUtils.getExtensionMimeType(gif)="
				+ getExtensionMimeType("gif"));
		System.out.println("FileMimeUtils.getExtensionMimeType(.pdf)="
				+ getExtensionMimeType(".pdf"));
		System.out.println("FileMimeUtils.getExtensionMimeType(.xls)="
				+ getExtensionMimeType(".xls"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.gif)="
				+ getFileMimeType("foo.gif"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.pdf)="
				+ getFileMimeType("foo.pdf"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.xls)="
				+ getFileMimeType("foo.xls"));
		System.out.println("FileMimeUtils.getFileMimeType(foo.badextension)="
				+ getFileMimeType("foo.badextension"));
	}
}