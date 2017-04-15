package com.safframework.queue;

import android.os.Bundle;

/**
 * Created by tony on 2017/4/15.
 */

public interface Operation {

    void run(Queue queue, Bundle bundle);
}
