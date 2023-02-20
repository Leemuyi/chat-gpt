package cn.com.mooyea.chatgpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author mooye
 */
@SpringBootApplication
@Slf4j
@EnableScheduling
public class ChatGptApplication {
//    private static final String DEFAULT_KEY = "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";
//    private static final String CONFIG_NAME = "openai-key";
//
//    private static final String CONFIG_FILE_NAME = "openai.yaml";

    public static void main(String[] args) {
        SpringApplication.run(ChatGptApplication.class, args);
//        String configFilePath = ProjectPathUtil.getProjectPath().replace("file:","")+File.separator+ CONFIG_FILE_NAME;
//        log.info("配置文件全路径:{}",configFilePath);
//        File configFile = new File(configFilePath);
//        if (!configFile.exists()) {
//            // 创建配置文件
//            try {
//                if (configFile.createNewFile()) {
//                    // 将默认 key 写入配置文件
//                    StringBuilder config = new StringBuilder();
//                    config.append("# 此处配置 openAI 的 api key,多个 api-key 请使用'[\"key1\",\"key2\"]'配置").append("\n").append(CONFIG_NAME).append(": ").append("[").append("\"").append(DEFAULT_KEY).append("\"").append("]");
//                    try(OutputStream out = new FileOutputStream(configFile)) {
//                        out.write(config.toString().getBytes(StandardCharsets.UTF_8));
//                    } catch (IOException e) {
//                        log.error("默认 api-key 写入失败,请检查 openai.yaml 是否存在及读写权限!");
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("创建配置文件失败,请检查目录是否有读写权限!");
//                return;
//            }
//        }
    }

}
