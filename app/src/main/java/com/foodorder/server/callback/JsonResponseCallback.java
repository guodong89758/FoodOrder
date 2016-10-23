package com.foodorder.server.callback;


import com.foodorder.log.DLOG;

import org.json.JSONObject;

public abstract class JsonResponseCallback extends ResponseCallback {
    public boolean onResponse(Object result, int httperr, String errmsg, int id, boolean fromcache) {
        if (httperr != 0 || result == null || !(result instanceof byte[])) {
            return onJsonResponse(null, httperr, errmsg, id, fromcache);
        }
        try {
            JSONObject json = new JSONObject(new String((byte[]) result));

            int code = json.optInt("code", -1);
            errmsg = json.optString("message");
            JSONObject data = json.optJSONObject("data");

            if (code == 200 && data != null) {
                return onJsonResponse(data, code, errmsg, id, fromcache);
            } else {
                return onJsonResponse(null, code, "", id, fromcache);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DLOG.d("json parse error!");
            return onJsonResponse(null, -1, "", id, fromcache);
        }
    }

    public abstract boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache);
}
