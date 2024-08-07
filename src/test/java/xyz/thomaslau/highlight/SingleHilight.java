package xyz.thomaslau.highlight;

import java.util.HashMap;
import java.util.Map;

public class SingleHilight {

    public static void main(String[] args) {
        String keyword = "vue";
        String result = new SingleHilight().highlight2(Utils.caseInsensitiveText, keyword);
        System.out.println(result);
    }

    public String highlight2(String content, String keyword) {
        StringBuilder result = new StringBuilder();
        Map<Integer, String> map = new HashMap<>();
        int startIndex = 0; 
        int endIndex = 0; 
        boolean isOpen = false; 
        for (int i = 0; i < content.length(); i++) { 
            for (char keyChar : keyword.toCharArray()) { 
                if (Character.toLowerCase(content.charAt(i)) == Character.toLowerCase(keyChar)) {
                    if (!isOpen) { 
                        startIndex = i; 
                        endIndex = i; 
                        isOpen = true; 
                    }
                    if (isOpen) { 
                        endIndex++; 
                    }
                    i = endIndex; 
                } else {
                    isOpen = false; 
                }
            }
            if (endIndex - startIndex == keyword.length()) {
                map.put(startIndex, "<em>");
                map.put(endIndex, "</em>");
                isOpen = false;

            }
        }
        for (int i = 0; i < content.length(); i++) {
            result.append(map.getOrDefault(i, "")).append(content.charAt(i));
        }
        result.append(map.getOrDefault(content.length(), ""));
        return result.toString();
    }
}
