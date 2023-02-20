package cn.com.mooyea.chatgpt.controller;

import cn.com.mooyea.chatgpt.config.RedisTemplateConfig;
import cn.com.mooyea.chatgpt.entity.system.SystemConfig;
import cn.com.mooyea.chatgpt.utils.ProjectPathUtil;
import cn.com.mooyea.chatgpt.utils.RedisTemplateUtil;
import cn.com.mooyea.chatgpt.utils.YamlUtil;
import cn.com.mooyea.chatgpt.vo.Result;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * <h1>SystemController<h1>
 * <p>Copyright (C), 星期一,20,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: SystemController</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/20</p>
 * <p>Description: 系统配置控制器</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>11:37 2023/2/20</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@RestController
@RequestMapping("/system")
@Slf4j
public class SystemController {
	@Resource
	RedisTemplateUtil redisTemplateUtil;

	@GetMapping("/init")
	public ModelAndView initModelAndView() {
		return new ModelAndView("init");
	}

	public static void main(String[] args) {
		log.info(ProjectPathUtil.getProjectPath());
	}
	@PostMapping("/showConfig")
	public Result<?> showConfiguration()  {
		Result<JSONObject> result = new Result<>();
		String redis = ProjectPathUtil.getProjectPath()+"redis.yaml";
		String config = ProjectPathUtil.getProjectPath()+"config.yaml";
		Integer type = (Integer) redisTemplateUtil.get("type",0);
		File redisFile = new File(redis);
		File configFile = new File(config);
		if (type == null){
			return Result.error("第一次启动,请先配置!");
		}
		if (redisFile.exists()){
			// TODO 处理保存方式为 redis 的配置;
			try {
				return handlerRedis(redisFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		if (configFile.exists()){
			// TODO 处理保存方式为 yaml 的配置;
			try {
				return handlerYaml(configFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	public Result<?> saveConfig(SystemConfig config){
		return null;
	}

	private Result<?> handlerYaml(File file) throws FileNotFoundException {
		Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(file));
		if (map != null) {
			String chatAuth = (String) map.get("chat_gpt_auth");
			Map<String, String> baiduYunMap = (Map<String, String>) map.get("baiduYun");
			String apiKey = baiduYunMap.get("ak");
			String secretKey = baiduYunMap.get("sk");
			SystemConfig systemConfig = SystemConfig.builder()
					.chatGptAuth(chatAuth).apiKey(apiKey).secretKey(secretKey).type(0)
					.build();
			return Result.OK("成功!", systemConfig);
		}
		return Result.error("配置文件尚未生成!");
	}

	private Result<?> handlerRedis(File file) throws FileNotFoundException {
		Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(file));
		if (map != null) {
			String host = (String) map.get("host");
			Integer port = (Integer) map.get("port");
			String auth = (String) map.get("password");
			Jedis jedis = new Jedis(host,port);
			//授权
			jedis.auth(auth);
			String chatAuth = jedis.get("chat_gpt_auth");
			String apiKey = jedis.get("ak");
			String secretKey = jedis.get("sk");

			SystemConfig systemConfig = SystemConfig.builder()
					.chatGptAuth(chatAuth).apiKey(apiKey).secretKey(secretKey).type(1)
					.build();
			return Result.OK("成功!", systemConfig);
		}
		return Result.error("配置文件尚未生成!");
	}

}
