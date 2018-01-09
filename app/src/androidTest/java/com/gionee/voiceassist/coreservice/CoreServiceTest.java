package com.gionee.voiceassist.coreservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

/**
 * Created by liyingheng on 1/3/18.
 */

@RunWith(AndroidJUnit4.class)
public class CoreServiceTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void test_service_started_on_bind() throws TimeoutException {

    }

}
