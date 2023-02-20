package top.mooyea.wordclouds.constant;

import java.util.regex.Pattern;

/**
 * <h1>RegConstant<h1>
 * <p>Copyright (C), 星期六,22,10月,2022</p>
 * <br/>
 * <hr>
 * <h3>File Info:</h3>
 * <p>FileName: RegConstant</p>
 * <p>Author:   mooye</p>
 * <p>Work_Email： lidy@skyvis.com.cn</p>
 * <p>E-mail： mooyeali@yeah.net</p>
 * <p>Date:     2022/10/22</p>
 * <p>Description: 正则静态类</p>
 * <hr>
 * <h3>History:</h3>
 * <hr>
 * <table>
 *  <thead>
 *  <tr><td style='width:100px;' center>Author</td><td style='width:200px;' center>Time</td><td style='width:100px;' center>Version_Number</td><td style='width:100px;' center>Description</td></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td style='width:100px;' center>mooye</td><td style='width:200px;' center>12:38 2022/10/22</td><td style='width:100px;' center>v_1.0.0</td><td style='width:100px;' center>创建</td></tr>
 *  </tbody>
 * </table>
 * <hr>
 * <br/>
 *
 * @author mooye
 */


public class RegConstant {
    public static final String REX = "\\[Reply:\\d+]";
    public static final String REX_EMOJI = "\\[[\\u4e00-\\u9fa5]+]*|\\[\\w+]";
    public static final Pattern PATTERN = Pattern.compile(REX);
    public static final Pattern EMOJI = Pattern.compile(REX_EMOJI);
}
