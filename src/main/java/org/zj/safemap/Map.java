package org.zj.safemap;

import java.util.HashMap;

/**
 * @BelongsProject: concurrenthashmap
 * @BelongsPackage: org.zj.safemap
 * @Author: ZhangJun
 * @CreateTime: 2018/11/14
 * @Description: 并发安全的map,每次插入锁slot
 */
public class Map<K,V> {
    //每个槽位的数量，如果超出就扩容槽位
    private int length;

    /**
     * 这里保存所有槽
     */
    private HashMap<K,V>[] slots;

    /**
     * 构造，接受参数每个槽位的最大容量，超出就扩容槽位
     * @param length
     */
    public Map(int length){
        this.length=length;
        slots=new HashMap[1];
    }


    /**
     * 插入数据
     * 每次插入都根据hash来选择要存放的槽位，并且是锁槽位的
     * @param k
     * @param v
     */
    public void insert(K k,V v){
        int i = k.hashCode()%slots.length;
        System.out.println("放到  "+i+"  卡槽");
        checkAndInitSlot(i);
        checkAndCapacitySlot();
        //然后你懂得
        if(slots[i]==null){
            synchronized (slots[i]) {
                if(slots[i]==null){
                    synchronized (slots[i]) {
                        System.out.println("你这个是空的，我需要new出来");
                        slots[i] =new HashMap<K, V>();
                    }
                }
            }
        }
        synchronized (slots[i]) {
            slots[i].put(k,v);
        }
    }

    private void checkAndInitSlot(int i) {
        if(slots[i]==null){
            slots[i]=new HashMap<K, V>();
        }
    }

    /**
     * 检查的并扩容
     */
    private void checkAndCapacitySlot() {
        System.out.println("检测扩容");
        if(slots[slots.length-1].size()==length-1){
            System.out.println("  开始扩容");
            capacitySlot();
            System.out.println("  扩容结束");
        }
    }

    /**
     * 对卡槽进行扩容
     */
    private void capacitySlot() {
        //2倍扩容
       HashMap<K,V>[] newSlots=new HashMap[slots.length*2];
        for(int i=0;i<slots.length;i++){
            newSlots[i]=slots[i];
        }
        slots=null;
        slots=newSlots;
    }

    /**
     * 搜索内容
     * @param k
     */
    public void search(K k){
        //先计算索引
        int i = k.hashCode() % slots.length;
        System.out.println(i);
        if(slots[i]==null){
            System.out.println("这个槽位没有初始化怎么搞的");
            return;
        }

       HashMap<K,V> slot = slots[i];
        System.out.println(slot.get(k));
    }

    /**
     * 从卡槽中移除指定key的键值对
     * @param k
     */
    public void delete(K k){
        int i = k.hashCode() % slots.length;
        if(slots[i]==null){
            System.out.println("你这个槽位没有初始化啊");
            return;
        }
        if(slots[i].size()==0){
            System.out.println("你这个槽位里面没有数据啊");
            return;
        }
        if(slots[i].get(k)==null){
            System.out.println("你这个槽位里面并没有用了这个key的键值对啊");
            return;
        }
        slots[i].remove(k);
        System.out.println("找到并移除了");
        return;
    }

}
