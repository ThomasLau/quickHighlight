package xyz.thomaslau.highlight;

import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;


public class AcAuto_Bugii {
    private static final int BYTE_SIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1;
    private final AcNode root = new AcNode();
    private final ArrayList<AcNode> nodes = new ArrayList<>();
    private int[] fail;

    public AcAuto_Bugii() {
        nodes.add(root);
    }

    /**
     * 求失败指针时 bfs 用
     */
    private final ArrayDeque<Integer> q = new ArrayDeque<>();

    private static class AcNode {
        /**
         * 模式串。<code>null</code> 表示此节点不是模式串的结尾。
         */
        String pattern = null;
        /**
         * 子节点在 {@link #nodes} 的下标。<code>-1</code> 表示相应 Byte 没有子节点。children 下标即代表 Byte 数据。
         */
        int[] children = new int[BYTE_SIZE];

        AcNode() {
            Arrays.fill(children, -1);
        }
    }

    /**
     * 插入一个模式串。模式串改变后需要调用 {@link #calcFail()} 重新计算失败指针。
     *
     * @param key 要匹配的模式串。
     */
    void insert(String key) {
        AcNode p = root;
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            int i = b - Byte.MIN_VALUE;
            if (p.children[i] == -1) {
                // 不包含所需节点，创建
                AcNode newNode = new AcNode();
                nodes.add(newNode);
                p.children[i] = nodes.size() - 1;
            }
            p = nodes.get(p.children[i]);
        }
        p.pattern = key;
    }

    void calcFail() {
        q.clear();
        fail = new int[nodes.size()];
        Arrays.fill(fail, -1);
        q.addLast(0);
        while (!q.isEmpty()) {
            int fatherIndex = q.pop();
            AcNode father = nodes.get(fatherIndex);
            for (int i = 0; i < father.children.length; i++) {
                if (father.children[i] == -1) {
                    continue;
                }
                int currIndex = father.children[i];
                int temp = fail[fatherIndex]; // temp 为当前节点 curr 父节点的失败指针
                while (temp != -1 && nodes.get(temp).children[i] == -1) {
                    // 若父亲的失败节点不为空，但是不存在公共后缀，则继续向上从这个节点的失败节点寻找
                    temp = fail[temp];
                }
                if (temp == -1) {
                    fail[currIndex] = 0;
                } else {
                    // 父亲的失败节点存在公共后缀，则将公共后缀的节点作为 curr 的失败节点
                    fail[currIndex] = nodes.get(temp).children[i];
                }
                q.addLast(currIndex);
            }
        }
        fail[0] = 0;
    }

    @FunctionalInterface
    interface MatchCallback {
        void onMatch(String key);
    }

    /**
     * 在给定串中查找模式串。
     * @param callback 每找到一个模式串后的回调。
     */
    void match(String str, MatchCallback callback) {
        if (str == null) {
            return;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        int pIndex = 0;
        int i = 0;
        while (i < bytes.length) {
            int b = bytes[i] - Byte.MIN_VALUE;
            if (nodes.get(pIndex).children[b] != -1) {
                pIndex = nodes.get(pIndex).children[b];
                AcNode p = nodes.get(pIndex);
                if (p.pattern != null) {
                    callback.onMatch(p.pattern);
                    // 匹配成功，下一轮从根开始匹配。因为要求每个字符只能被消耗一次（依需求而定）
                    pIndex = 0;
                }
                i++;
                continue;
            }
            if (pIndex == 0) {
                i++;
            }
            pIndex = fail[pIndex];
        }
    }

    public static void main(String[] args) {
        AcAuto_Bugii aac = new AcAuto_Bugii();
        Utils.keywords.forEach(key -> aac.insert(key));
        aac.calcFail();
        aac.match(Utils.text, s -> {
            System.out.println(String.format("%s", s));
        });
    }
}