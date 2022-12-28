package com.iflytek.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClick(View view) {
        //跳转到我的资料卡
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=2222106868")));//跳转到QQ资料
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&card_type=group&source=qrcode&uin=140203620")));//跳转到QQ群
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&version=1&uin=2222106868")));//跳转到临时会话
    }

    void chat_type() {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=2222106868";
            //uin是发送过去的qq号码
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "您还没有安装QQ，请先安装软件", Toast.LENGTH_SHORT).show();
        }
    }

    void chat_group() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "OqjzAUUqnMPY8uXfQ4qCafrQ6SjL1OD3"));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "您还没有安装QQ，请先安装软件", Toast.LENGTH_SHORT).show();
        }
    }
}

