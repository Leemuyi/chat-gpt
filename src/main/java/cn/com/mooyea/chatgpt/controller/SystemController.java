package cn.com.mooyea.chatgpt.controller;

import cn.com.mooyea.chatgpt.config.RedisTemplateConfig;
import cn.com.mooyea.chatgpt.entity.system.SystemConfig;
import cn.com.mooyea.chatgpt.utils.ProjectPathUtil;
import cn.com.mooyea.chatgpt.utils.RedisTemplateUtil;
import cn.com.mooyea.chatgpt.utils.YamlUtil;
import cn.com.mooyea.chatgpt.vo.Result;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
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
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
	private static final String CONFIG = ProjectPathUtil.getProjectPath()+"/config.yaml";

	@GetMapping("/init")
	public ModelAndView initModelAndView() {
		return new ModelAndView("init");
	}

	@PostMapping("/showConfig")
	public Result<?> showConfiguration()  {
		Result<JSONObject> result = new Result<>();
		log.info(CONFIG);
		Integer type = (Integer) redisTemplateUtil.get("type",0);
		File configFile = new File(CONFIG);
		if (!configFile.exists() && type == null){
			return Result.error("第一次启动,请先配置!");
		}
		if (configFile.exists()){
			try {
				return handlerYaml(configFile);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return handlerRedis();
	}
	@PostMapping("/saveConfig")
	public Result<?> saveConfig(SystemConfig config){
		if (Objects.equals(config.getType(),0)){
			return saveYamlConfig(config);
		}
		if (Objects.equals(config.getType(),1)){
			return saveRedisConfig(config);
		}
		return Result.error("未知错误!");
	}





	private Result<?> saveRedisConfig(SystemConfig config) {
		redisTemplateUtil.set("chat_gpt_auth",config.getChatGptAuth(),0);
		redisTemplateUtil.set("ak",config.getApiKey(),0);
		redisTemplateUtil.set("sk",config.getSecretKey(),0);
		redisTemplateUtil.set("type",config.getType(),0);
		// 判断 yaml 是否存在
		File yaml = new File(CONFIG);
		if (yaml.exists()){
			if (yaml.delete()) {
				return Result.OK("配置保存成功!");
			}else {
				return Result.OK("配置保存成功!但原始文件删除失败,请手动删除该文件,文件路径:"+CONFIG);
			}
		}
		return Result.OK("保存成功!");
	}

	private Result<?> saveYamlConfig(SystemConfig config)  {
		Map<String,Object> map = null;
		try {
			map = YamlUtil.getConfig(new FileInputStream(CONFIG));
		} catch (FileNotFoundException e) {
			File file = new File(CONFIG);
			try {
				if (!file.createNewFile()) {
					return Result.error("配置文件创建失败!");
				}
				map = YamlUtil.getConfig(new FileInputStream(CONFIG));
			} catch (IOException ex) {
				return Result.error("配置文件创建失败!");
			}
		}
		if (map==null) {
			map = new HashMap<>(16);
		}
		map.put("chat_gpt_auth",config.getChatGptAuth());
		map.put("ak",config.getApiKey());
		map.put("sk",config.getSecretKey());
		return YamlUtil.updateConfig(map,CONFIG);
	}


	private Result<?> handlerYaml(File file) throws FileNotFoundException {
		Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(file));
		if (map != null) {
			String chatAuth = (String) map.get("chat_gpt_auth");
			String apiKey = (String) map.get("ak");
			String secretKey = (String) map.get("sk");
			SystemConfig systemConfig = SystemConfig.builder()
					.chatGptAuth(chatAuth).apiKey(apiKey).secretKey(secretKey).type(0)
					.build();
			return Result.OK("成功!", systemConfig);
		}
		return Result.error("配置文件尚未生成!");
	}

	private Result<?> handlerRedis() {
		Integer type = (Integer) redisTemplateUtil.get("type",0);
		if (type != null) {
			String chatAuth = (String) redisTemplateUtil.get("chat_gpt_auth", 0);
			String apiKey = (String) redisTemplateUtil.get("ak", 0);
			String secretKey = (String) redisTemplateUtil.get("sk", 0);
			SystemConfig systemConfig = SystemConfig.builder()
					.chatGptAuth(chatAuth).apiKey(apiKey).secretKey(secretKey).type(type)
					.build();
			return Result.OK("成功!", systemConfig);
		}
		return Result.error("未读取到配置");
	}

}
