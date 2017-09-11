/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Przemek
 */
public class Message {
    private long peopleId;
    private String msg;
    private String time;

    public Message() {
    }

    public Message(String msg, long peopleId, String time) {
        this.msg = msg;
        this.peopleId = peopleId;
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(long peopleId) {
        this.peopleId = peopleId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{"
                + ", peopleId='" + peopleId + '\''
                + ", msg='" + msg + '\''
                + ", time='" + time + '\''
                + '}';
    }
}
