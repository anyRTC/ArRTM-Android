package org.ar.rtm;

public class MessageBean {
    public String name;
    public String content;
    public boolean isSelf;

    public MessageBean(String name, String content,boolean isSelf) {
        this.name = name;
        this.content = content;
        this.isSelf=isSelf;
    }
}
