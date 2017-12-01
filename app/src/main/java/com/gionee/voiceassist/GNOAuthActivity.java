/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gionee.voiceassist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.duer.dcs.http.HttpConfig;
import com.baidu.duer.dcs.oauth.api.grant.BaiduDialogError;
import com.baidu.duer.dcs.oauth.api.grant.BaiduException;
import com.baidu.duer.dcs.oauth.api.grant.BaiduOauthImplicitGrantIml;
import com.baidu.duer.dcs.systeminterface.IOauth;

import java.util.HashMap;

/**
 * 用户认证界面
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/18.
 */
public class GNOAuthActivity extends GNBaseActivity implements View.OnClickListener {
    // 需要开发者自己申请client_id
    // client_id，就是oauth的client_id
    private static final String CLIENT_ID = "d8ITlI9aeTPaGcxKKsZit8tq";
    // 是否每次授权都强制登陆
    private boolean isForceLogin = false;
    // 是否每次都确认登陆
    private boolean isConfirmLogin = true;
    private EditText editTextClientId;
    private Button oauthLoginButton;
    private IOauth iOauthImpl;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GNOAuthActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dcs_sample_activity_oauth);
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        oauthLoginButton.setOnClickListener(this);
    }

    private void initView() {
        editTextClientId = (EditText) findViewById(R.id.edit_client_id);
        oauthLoginButton = (Button) findViewById(R.id.btn_login);

        editTextClientId.setText(CLIENT_ID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String clientId = editTextClientId.getText().toString();
                if (!TextUtils.isEmpty(clientId) && !TextUtils.isEmpty(clientId)) {
                    iOauthImpl = new BaiduOauthImplicitGrantIml(clientId, GNOAuthActivity.this);
                    iOauthImpl.getToken(isForceLogin, isConfirmLogin, new IOauth.BaiduOauthListener() {
                        @Override
                        public void onCancel() {
                            Toast.makeText(GNOAuthActivity.this.getApplicationContext(), GNOAuthActivity.this.getResources()
                                            .getString(R.string.login_canceled),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(BaiduDialogError e) {
                            if (null != e) {
                                String toastString = TextUtils.isEmpty(e.getMessage())
                                        ? GNOAuthActivity.this.getResources()
                                        .getString(R.string.login_failed) : e.getMessage();
                                Toast.makeText(GNOAuthActivity.this.getApplicationContext(), toastString,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onException(BaiduException e) {
                            if (null != e) {
                                String toastString = TextUtils.isEmpty(e.getMessage())
                                        ? GNOAuthActivity.this.getResources()
                                        .getString(R.string.login_failed) : e.getMessage();
                                Toast.makeText(GNOAuthActivity.this.getApplicationContext(), toastString,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSucceed(HashMap<String, String> hashMap) {
                            // 设置accessToken
                            HttpConfig.setAccessToken(hashMap.get("access_token"));
                            Toast.makeText(GNOAuthActivity.this.getApplicationContext(),
                                    getResources().getString(R.string.login_succeed),
                                    Toast.LENGTH_SHORT).show();
                            startMainActivity();
                        }
                    });
                } else {
                    Toast.makeText(GNOAuthActivity.this.getApplicationContext(),
                            getResources().getString(R.string.client_id_empty),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(GNOAuthActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}