package com.yoctu.notif.android.yoctulibrary;

import com.yoctu.notif.android.yoctulibrary.models.Notification;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gael on 25.05.18.
 */

public class NotificationUnitTest {

    @Test
    public void notificationIsNull() {
        Notification notification = null;
        assertEquals(null,notification);
    }

    @Test
    public void notificationIsNotNull() {
        Notification notification = new Notification();
        assertNotEquals(null,notification);
    }

    @Test
    public void notificationTitleEmpty() {
        Notification notification = new Notification();
        assertEquals("",notification.getTitle());
    }

    @Test
    public void notificationTitleNotEmpty() {
        String myTitle = "Hello !";
        Notification notification = new Notification();
        notification.setTitle(myTitle);
        assertEquals(myTitle,notification.getTitle());
    }

    @Test
    public void notificationBodyEmpty() {
        Notification notification = new Notification();
        assertEquals("",notification.getBody());
    }

    @Test
    public void notificationBodyNotEmpty() {
        String myBody = "Test body !";
        Notification notification = new Notification();
        notification.setBody(myBody);
        assertEquals(myBody,notification.getBody());
    }

    @Test
    public void notificationTopicEmpty() {
        Notification notification = new Notification();
        assertEquals("",notification.getTopic());
    }

    @Test
    public void notificationTopicNotEmpty() {
        String myTopic = "TOPIC";
        Notification notification = new Notification();
        notification.setTopic(myTopic);
        assertEquals(myTopic,notification.getTopic());
    }

    @Test
    public void notificationTimeEmpty() {
        Notification notification = new Notification();
        assertEquals(0,notification.getTime());
    }

    @Test
    public void notificationTimeNotEmpty() {
        long myTime = 12589741236L;
        Notification notification = new Notification();
        notification.setTime(myTime);
        assertEquals(myTime,notification.getTime());
    }

    @Test
    public void createNotificationWithoutTopic() {
        Notification notification = new Notification("my title","my body");
        assertEquals("",notification.getTopic());
    }

    @Test
    public void createNotificationWithTopic() {
        String myTopic = "TOPIC";
        Notification notification = new Notification("my title","my body",myTopic
        );
        assertEquals(myTopic,notification.getTopic());
    }
}
