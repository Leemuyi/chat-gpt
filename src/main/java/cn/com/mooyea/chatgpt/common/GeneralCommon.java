package cn.com.mooyea.chatgpt.common;

import cn.com.mooyea.chatgpt.utils.ProjectPathUtil;
import cn.com.mooyea.chatgpt.utils.RedisTemplateUtil;
import cn.com.mooyea.chatgpt.utils.YamlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>GeneralCommon<h1>
 * <p>Copyright (C), 星期二,21,2月,2023</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: GeneralCommon</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2023/2/21</p>
 * <p>Description: 一些通用的操作</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>10:54 2023/2/21</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */

@Component
@Slf4j
public class GeneralCommon {
	@Resource
	RedisTemplateUtil redisTemplateUtil;
	private static final String CONFIG = ProjectPathUtil.getProjectPath()+"/config.yaml";


	@SneakyThrows(value = FileNotFoundException.class)
	public String setBaiduToken(String ak, String sk){
		double now = System.currentTimeMillis()/1000.0;
		String https = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="+ak+"&client_secret="+sk;
		String res = HttpUtil.get(https);
		log.info(res);
		if (!res.contains("error_description")){
			JSONObject json = JSONUtil.parseObj(res);
			String token = json.getStr("access_token");
			int expiresIn = json.getInt("expires_in");
			if (new File(CONFIG).exists()){
				Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
				map.put("access_token",token);
				map.put("expires_in",(expiresIn+ now)*1000);
				YamlUtil.updateConfig(map,CONFIG);
			}else {
				redisTemplateUtil.set("access_token",token,0,expiresIn);
			}
			return token;
		}else {
			return "0";
		}
	}

	@SneakyThrows(value = FileNotFoundException.class)
	public String getBaiduToken(String ak,String sk){
		String token;
		if (new File(CONFIG).exists()){
			Map<String,Object> map = YamlUtil.getConfig(new FileInputStream(CONFIG));
			token = (String) map.get("access_token");
			if (token == null){
				setBaiduToken(ak, sk);
				getBaiduToken(ak, sk);
			}
			long time = (long) map.get("expires_in");
			if (System.currentTimeMillis()<time){
				return token;
			}
		}else {
			token = (String) redisTemplateUtil.get("access_token",0);
			if (token != null){
				return token;
			}
		}
		return setBaiduToken(ak,sk);
	}

	public String readConfig(String key){
		File configFile = new File(CONFIG);
		if (configFile.exists()){
			try {
				Map<String,Object> yaml = YamlUtil.getConfig(new FileInputStream(configFile));
				return (String) yaml.get(key);
			} catch (FileNotFoundException e) {
				return (String) redisTemplateUtil.get(key,0);
			}
		}else {
			return (String) redisTemplateUtil.get(key,0);
		}
	}

	public int textVerification(String token,String text){
		String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token="+token;
		String param = "text=" + text;
		String res = HttpUtil.post(url,param);
		log.info(res);
		if (!res.contains("error_code")){
			JSONObject json = JSONUtil.parseObj(res);
			return json.getInt("conclusionType");
		}else {
			return 5;
		}

	}

	public String getResponseText(String text){
		String responseText = "";
		String url = "https://v1.apigpt.cn/?q="+text+"&apitype=sql";
		String res = HttpUtil.get(url);
		log.info("请求结果:{}",res);
		if (res != null && res.contains("<!DOCTYPE html>")){
			return text+"\nApi服务异常,请稍后再试!";
		}
		JSONObject json = JSONUtil.parseObj(res);
		if (Objects.equals(json.getInt("code"), HttpStatus.HTTP_OK)){
			if (json.containsValue("敏感内容，无法展示")){
				return "0";
			}
			responseText = json.getStr("ChatGPT_Answer")==null?"网络异常,请稍后再试!":json.getStr("ChatGPT_Answer");
		}else {
			responseText = "网络异常,请稍后再试!";
		}
		return json.getStr("Questions")+responseText;
	}

//	public static void main(String[] args) {
//		log.info(new GeneralCommon().getResponseText("你好骚啊?"));
//	}
}