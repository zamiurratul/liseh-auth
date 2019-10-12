package com.liseh.auth.util;

import java.util.UUID;

public class CommonUtil {
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
