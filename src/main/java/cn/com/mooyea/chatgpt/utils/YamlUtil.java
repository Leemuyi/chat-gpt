package cn.com.mooyea.chatgpt.utils;

import cn.com.mooyea.chatgpt.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * <h1>YamlUtil<h1>
 * <p>Copyright (C), 星期一,20,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: YamlUtil</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/20</p>
 * <p>Description: yaml 文件读写工具类</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>14:49 2023/2/20</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@Slf4j
public class YamlUtil {
	public static Map<String, Object> getConfig(InputStream stream){
		Yaml yaml = new Yaml();
		return yaml.load(stream);
	}

	public static Result<?> updateConfig(Map<String, Object> yamlMap,String path,String key,Object value){
		Yaml yaml = new Yaml();
		if (yamlMap!=null) {
			try (FileWriter fileWriter = new FileWriter(path)) {
				log.info("配置:{}", yamlMap);
				// 修改配置
				Map<String, Object> updataMap = (Map<String, Object>) yamlMap.get("qq-logs");
				updataMap.put(key, value);
				// 用yaml方法把map结构格式化为yaml文件结构 重新写入yaml文件
				fileWriter.write(yaml.dumpAsMap(yamlMap));
				// 刷新
				fileWriter.flush();
				// 关闭流
			} catch (IOException e) {
				return Result.error(e.getMessage());
			}
			return Result.OK("配置保存成功!");
		}else {
			return Result.error("未读取到配置文件!");
		}
	}
}
