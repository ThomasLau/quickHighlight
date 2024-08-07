package org.ugc.springtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MLightUtils {
    public static void main(String[] args) {
        String text = "我想买苹果手机，iphone6 请问哪里可以买苹果手机";
        List<String> keywords = Arrays.asList("苹果", "苹果手机", "哪里", "6", "iphonec6");
        System.out.println(getMRedText(keywords, text, null, null));
    }

    /**
     * @param list 数据源匹配 例如： {"难的","这世界","世界上最", "世界上最难", "最难的职业","意志","坚韧不拔","职业就是","程序员","他们"}
     * @param str 你所要处理的字符串 例如： 世界上最难的职业
     * @param leftTag 左标签字符串 例如：<font style='color:red;'>
     * @param rightTag 右标签字符串 例如：</font>
     * @return
     */
    public static String getMRedText(List<String> list, String str, String leftTag, String rightTag) {
        if (leftTag == null || "".equals(leftTag))
            leftTag = "<font style='color:red;'>";
        if (rightTag == null || "".equals(rightTag))
            rightTag = "</font>";

        List<MLight> l = new ArrayList<>();

        for (String item : list) {
            if (str.contains(item)) {
                int startIndex = str.indexOf(item);
                int endIndex = startIndex + item.length() - 1;
                l.add(new MLight(startIndex, 0, leftTag));
                l.add(new MLight(endIndex, 1, rightTag));
            }
        }

        if (l.size() == 0)
            return str;

        Collections.sort(l, (o1, o2) -> {
            if (o1.getIndex().equals(o2.getIndex()))
                return o2.getType() - o1.getType();
            return o1.getIndex() - o2.getIndex();
        });

        char[] strChars = str.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strChars.length; i++) {
            char item = strChars[i];
            List<MLight> currentIndexMLights = getCurrentMReds(i, l);
            if (currentIndexMLights.size() == 0) {
                sb.append(item);
                continue;
            }

            for (MLight item0 : currentIndexMLights) {
                if (item0.getType() == 0)
                    sb.append(item0.getTagTxt());
            }

            sb.append(item);

            for (MLight item0 : currentIndexMLights) {
                if (item0.getType() == 1)
                    sb.append(item0.getTagTxt());
            }

        }

        return sb.toString();
    }

    // 获取某个位置下的 高亮对象
    private static List<MLight> getCurrentMReds(Integer index, List<MLight> list) {
        List<MLight> rs = new ArrayList<>();
        for (MLight mLight : list) {
            if (mLight.getIndex().equals(index)) {
                rs.add(mLight);
            }
        }
        return rs;
    }

    // 高亮显示类实体
    static class MLight {
        private Integer index;// 目标字符串的 下标
        private Integer type;// 0代表左边，1代表右边
        private String tagTxt;// 标签文本字符串

        public String getTagTxt() {
            return tagTxt;
        }

        public void setTagTxt(String tagTxt) {
            this.tagTxt = tagTxt;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public MLight(Integer index, Integer type, String tagTxt) {
            this.index = index;
            this.type = type;
            this.tagTxt = tagTxt;
        }
    }
}
