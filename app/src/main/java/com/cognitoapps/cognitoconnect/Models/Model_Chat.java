package com.cognitoapps.cognitoconnect.Models;

public class Model_Chat {


    String identity,status,chat_id;

    public Model_Chat(){}


    public Model_Chat(String identity,String status,String chat_id) {
       this.status = status;
       this.identity = identity;
       this.chat_id = chat_id;

    }



    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;

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
