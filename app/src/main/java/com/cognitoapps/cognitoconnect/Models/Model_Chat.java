package com.cognitoapps.cognitoconnect.Models;

public class Model_Chat {


    String identity,created,chat_id;

    public Model_Chat(){}


    public Model_Chat(String identity,String created,String chat_id) {
       this.created = created;
       this.identity = identity;
       this.chat_id = chat_id;

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


}
