package com.lld.im.service.utils;

/**
 * @author tangcj
 * @date 2023/06/04 19:49
 **/
public class ConversationIdGenerate {

    //A|B
    //B A
    public static String generateP2PId(String fromId,String toId){
        int i = fromId.compareTo(toId);
        if(i < 0){
            return toId+"|"+fromId;
        }else if(i > 0){
            return fromId+"|"+toId;
        }
        throw new RuntimeException("");
    }
}
