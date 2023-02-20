package cn.com.mooyea.chatgpt.entity.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>SystemConfig<h1>
 * <p>Copyright (C), 星期一,20,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: SystemConfig</p>
 * <p>Author:   mooye</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/20</p>
 * <p>Description: 配置页实体类</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>11:16 2023/2/20</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig {
	/**
	 * ChatGPT Authorization
	 */
	private String chatGptAuth;
	/**
	 * 百度云应用的AK
	 */
	private String apiKey;
	/**
	 * 百度云应用的SK
	 */
	private String secretKey;
	/**
	 * 存储方式,0:配置文件,1:redis 存储
	 */
	private Integer type;
	/**
	 * redis 主机 ip
	 */
	private String host;
	/**
	 * redis 主机访问端口
	 */
	private Integer port;
	/**
	 * redis 密码
	 */
	private String redisAuth;
}
