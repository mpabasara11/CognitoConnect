package com.cognitoapps.cognitoconnect.Models;

public class Model_Chat {


    String identity,created,chat_id;
    boolean face_exp,last_seen;

    public Model_Chat(){}

    public Model_Chat(String identity, String created, String chat_id, boolean face_exp, boolean last_seen) {
        this.identity = identity;
        this.created = created;
        this.chat_id = chat_id;
        this.face_exp = face_exp;
        this.last_seen = last_seen;
    }

    public String getCreated() {

       return created;

    }

    public void setCreated(String created) {
        this.created = created;


    }


    public String getIdentity() {
     return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;

    }

    public String getChat_id(){return chat_id;}

    public void setChat_id(String chat_id)
    {
        this.chat_id = chat_id;
    }


    public boolean getFace_exp() {
        return face_exp;
    }

    public void setFace_exp(boolean face_exp) {
        this.face_exp = face_exp;
    }

    public boolean getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(boolean last_seen) {
        this.last_seen = last_seen;
    }
}
