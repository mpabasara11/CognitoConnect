package com.cognitoapps.cognitoconnect.Models;

public class Model_Chat_Data {



    String identity;
    String emotion;

    String last_seen;
    public Model_Chat_Data()
    {

    }

    public Model_Chat_Data(String identity, String emotion,String last_seen) {
        this.identity = identity;
        this.emotion = emotion;
        this.last_seen = last_seen;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }
}
