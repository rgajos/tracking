/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Radek
 */
public class Notification {

    private String time;
    private String msg;
    private long notificationPeopleId;
    private int notificationType;
    private double speed;

    public Notification() {
    }

    public Notification(String msg, long notificationPeopleId, String time, int notificationType, double speed) {
        this.msg = msg;
        this.notificationPeopleId = notificationPeopleId;
        this.time = time;
        this.notificationType = notificationType;
        this.speed = speed;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getNotificationPeopleId() {
        return notificationPeopleId;
    }

    public void setNotificationPeopleId(long notificationPeopleId) {
        this.notificationPeopleId = notificationPeopleId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", notificationPeopleId=" + notificationPeopleId +
                ", notificationType=" + notificationType +
                ", speed=" + speed +
                '}';
    }
}
