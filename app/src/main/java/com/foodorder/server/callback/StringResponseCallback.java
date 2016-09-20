package com.foodorder.server.callback;

import android.text.TextUtils;

import com.nextjoy.library.log.DLOG;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class StringResponseCallback extends ResponseCallback {
	@Override
	public boolean onResponse(Object resultInfo, int httpErr, String errMsg, int id, boolean fromCache) {
		if (httpErr != 0 || resultInfo == null || !(resultInfo instanceof byte[])) {
			return onStringResponse(null, httpErr, errMsg, id, fromCache);
		}
		JSONObject json;

		try {
			json = new JSONObject(new String((byte[]) resultInfo));
				int	code = json.optInt("status", -1);
			//modify by liuhao 2016-5-17添加数据返回 resultInfo 值为"" 对象不为null时候的判断导致崩溃的问题
			if (resultInfo == null || !(resultInfo instanceof byte[]) || TextUtils.isEmpty(resultInfo.toString())) {
				onStringResponse(null, code, "", id, fromCache);
			} else {
				onStringResponse(new String((byte[]) resultInfo), code, "", id, fromCache);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			DLOG.d("json parse error!");
			return onStringResponse(null, -1, "", id, fromCache);
		}
		return true;
	}

	public abstract boolean onStringResponse(String result, int errCode, String errMsg, int id, boolean formCache);
}
