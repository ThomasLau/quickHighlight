package org.ugc.springtool;

import java.util.HashMap;
import java.util.Map;

/**
 * 高亮字符串中的关键字
 * @author huhailong
 *
 */
public class HighlightKeyword {

    public static void main(String[] args) {
        String content = "使用Vite搭建Vue3+ElementUI-Plus项目过程，vue2迁移VuE3,更多内容请访问Vue官网";
        String keyword = "vue";
        String result = new HighlightKeyword().highlight2(content, keyword);
        System.out.println(result);
    }

    public String highlight2(String content, String keyword) {
        StringBuilder result = new StringBuilder();
        Map<Integer, String> map = new HashMap<>();
        int startIndex = 0; // 关键字起始索引
        int endIndex = 0; // 关键字结尾索引
        boolean isOpen = false; // 进入关键字匹配标志
        for (int i = 0; i < content.length(); i++) { // 遍历原始字符串
            for (char keyChar : keyword.toCharArray()) { // 遍历关键字字符串
                if (Character.toLowerCase(content.charAt(i)) == Character.toLowerCase(keyChar)) {
                    if (!isOpen) { // 匹配到关键字第一个字符相等后
                        startIndex = i; // 将起始索引赋值为当前遍历原始字符串索引
                        endIndex = i; // 将结尾索引也赋值为相同到当前索引
                        isOpen = true; // 标记进入匹配模式
                    }
                    if (isOpen) { // 判断是否处于匹配模式
                        endIndex++; // 将结尾索引自增
                    }
                    i = endIndex; // 将遍历原始字符串到索引定位到结尾索引，避免重复遍历
                } else {
                    isOpen = false; // 如果不相等则结束匹配模式
                }
            }
            if (endIndex - startIndex == keyword.length()) { // 如果结束索引与起始索引相减到值为关键字到长度则表明匹配到完整到关键字
                // 将起始索引和结束索引对应到高亮标签put到哈希表中，并且重置匹配标识
                map.put(startIndex, "<font style='color:red'>");
                map.put(endIndex, "</font>");
                isOpen = false;

            }
        }
        // 遍历原始字符串，通过哈希表中存储到高亮索引，将标签拼接到原始字符串里面
        for (int i = 0; i < content.length(); i++) {
            result.append(map.getOrDefault(i, "")).append(content.charAt(i));
        }
        // 下面这一行是用来处理特殊情况，即关键字在最后到情况
        result.append(map.getOrDefault(content.length(), ""));
        return result.toString();
    }
}
