package org.zj.safemap;

/**
 * @BelongsProject: concurrenthashmap
 * @BelongsPackage: org.zj.safemap
 * @Author: ZhangJun
 * @CreateTime: 2018/11/14
 * @Description: ${Description}
 */
public class Content {
    public static void main(String[] args) {
        Map<String,String> m=new Map<String, String>(3);

        m.insert("z","22");

        m.insert("b","23");

        m.insert("d","24");

        m.insert("i","22");

        m.insert("o","64");

        m.search("i");
        m.delete("i");
        m.search("i");
    }
}
