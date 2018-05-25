package com.yoctu.notif.android.yoctulibrary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by gael on 25.05.18.
 */

public class LibraryUtilsUnitTest {

    @Test
    public void testFormatDateValid() {
        assertNotEquals("",LibraryUtils.INSTANCE.formatDate(1527243511));
    }
    
}
