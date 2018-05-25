package com.yoctu.notif.android.yoctuappnotif;

import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils;
import com.yoctu.notif.android.yoctulibrary.models.Notification;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gael on 25.05.18.
 */

public class YoctuUtilsUnitTest {

    @Test
    public void testEmptyURL() {
        String myURL = "";
        Boolean result = YoctuUtils.INSTANCE.isValidScheme(myURL);
        assertEquals(result,false);
    }

    @Test
    public void testInvalidURL() {
        String myURL = "http";
        Boolean result = YoctuUtils.INSTANCE.isValidScheme(myURL);
        assertEquals(result,false);
    }

    @Test
    public void testInvalidURLWithFirstPart() {
        String myURL = "https://";
        Boolean result = YoctuUtils.INSTANCE.isValidScheme(myURL);
        assertEquals(result,false);
    }

}
