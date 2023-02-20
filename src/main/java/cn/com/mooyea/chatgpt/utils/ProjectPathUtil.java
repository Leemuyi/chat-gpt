package cn.com.mooyea.chatgpt.utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * <h1>ProjectPathUtil<h1>
 * <p>Copyright (C), 星期二,13,12月,2022</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: ProjectPathUtil</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2022/12/13</p>
 * <p>Description: 获取项目所在路径</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>17:53 2022/12/13</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */


public class ProjectPathUtil {
	private static final String PROJECT_NAME = "chat-gpt-0.0.1.jar";

	public static String getProjectPath() {

		URL url = ProjectPathUtil.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath != null && filePath.contains(PROJECT_NAME)) {
			filePath = filePath.split(PROJECT_NAME)[0];
		}
		File file = new File(filePath);
		filePath = file.getPath();
		return filePath;
	}
}
