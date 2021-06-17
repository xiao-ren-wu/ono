package org.ywb.ono.toolkit;

import java.util.List;
import java.util.Objects;

/**
 * @author yuwenbo1
 * @version v1.0.0
 * <p>
 * 将uri以树的形式存储起来
 * </p>
 * @date 2019/9/3 9:39
 */
public final class UriTree {

    /**
     * 通配符
     */
    private final String ANY = "**";

    private final Node ANY_NODE = new Node(ANY);

    /**
     * 树的根节点
     */
    private Node root;

    /**
     * 树的节点
     */
    private static class Node {
        /**
         * 节点存储的内容
         */
        String chips;
        /**
         * 是否结束
         */
        boolean isEnd;
        /**
         * nextLevel：该节点的儿子节点
         * next：该节点的兄弟节点
         */
        Node nextLevel, next;

        Node(String chips) {
            this.chips = chips;
            nextLevel = null;
            next = null;
            isEnd = false;
        }
    }

    public UriTree() {
        root = new Node("root");
    }

    /**
     * 添加uri到树中，
     * 如果uri中含有**符号，树会自动剪枝
     *
     * @param uri URI
     * @return boolean
     */
    private void add(String uri) {
        if (uri == null || "".equals(uri)) {
            return;
        }
        uri = uri.trim();
        String[] split = uri.split("/");

        Node c = root;
        for (String s : split) {
            if (ANY.equals(s)) {
                c.nextLevel = ANY_NODE;
                return;
            } else {
                if (Objects.isNull(c.nextLevel)) {
                    c.nextLevel = new Node(s);
                    c = c.nextLevel;
                } else if (c.nextLevel.chips.equals(ANY)) {
                    return;
                } else {
                    c = c.nextLevel;
                    while (!c.chips.equals(s) && c.next != null) {
                        c = c.next;
                    }
                    if (!c.chips.equals(s)) {
                        c.next = new Node(s);
                        c = c.next;
                    }
                }
            }
        }
        c.isEnd = true;
    }


    /**
     * 判断uri在该树中
     *
     * @param uri uri
     * @return boolean
     */
    public boolean contains(String uri) {
        if (root.nextLevel == null || uri == null || "".equals(uri)) {
            return false;
        }
        uri = uri.trim();
        String[] split = uri.split("/");

        Node c = root;
        for (String s : split) {
            if (c.nextLevel == null) {
                return false;
            } else {
                if (c.nextLevel.chips.equals(ANY)) {
                    return true;
                } else {
                    c = c.nextLevel;
                    while (!c.chips.equals(s) && c.next != null) {
                        c = c.next;
                    }
                    if (!c.chips.equals(s)) {
                        return false;
                    }
                }
            }
        }
        return c.nextLevel == null || c.nextLevel.chips.equals(ANY)|| c.isEnd;
    }

    /**
     * 通过uriList构造UriTree
     *
     * @param uriList list
     * @return org.ywb.ono.toolkit.UriTree
     */
    public static UriTree create(List<String> uriList) {
        UriTree uriTree = new UriTree();
        uriList.forEach(uriTree::add);
        return uriTree;
    }
}
