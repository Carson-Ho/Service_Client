package scut.carson_ho.service_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import scut.carson_ho.service_server.AIDL_Service1;


public class MainActivity extends AppCompatActivity {

        private Button bindService;

        //定义aidl接口变量
        private AIDL_Service1 mAIDL_Service;

        //创建ServiceConnection的匿名类
        private ServiceConnection connection = new ServiceConnection() {

            //重写onServiceConnected()方法和onServiceDisconnected()方法
            //在Activity与Service建立关联和解除关联的时候调用
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            //在Activity与Service建立关联时调用
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                //使用AIDLService1.Stub.asInterface()方法将传入的IBinder对象传换成了mAIDL_Service对象
                mAIDL_Service = AIDL_Service1.Stub.asInterface(service);

                try {

                    //通过该对象调用在MyAIDLService.aidl文件中定义的接口方法,从而实现跨进程通信
                    mAIDL_Service.AIDL_Service();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            bindService = (Button) findViewById(R.id.bind_service);

            //设置绑定服务的按钮
            bindService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("点击了[绑定服务]按钮");

                    //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
                    //参数与服务器端的action要一致,即"服务器包名.aidl接口文件名"
                    Intent intent = new Intent("scut.carson_ho.service_server.AIDL_Service1");

                    //Android5.0后无法只通过隐式Intent绑定远程Service
                    //需要通过setPackage()方法指定包名
                    intent.setPackage("scut.carson_ho.service_server");

                    //绑定服务,传入intent和ServiceConnection对象
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);

                }
            });
        }

    }
