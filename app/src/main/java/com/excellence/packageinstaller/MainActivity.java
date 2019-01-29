package com.excellence.packageinstaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.excellence.packageinstaller.accessibilityserviceorroot.AutoInstaller;
import com.excellence.packageinstaller.silent.ShellUtil;
import com.excellence.packageinstaller.silent.SilentInstaller;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private File mFile = new File("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Root、辅助安装
        AutoInstaller.getDefault(MainActivity.this).install(mFile);

        // 原生
        originPackageInstaller();

        // 系统权限的静默安装
        systemPackageInstaller();

        // 系统隐藏API
        systemHidePackageInstaller();

    }

    private void systemHidePackageInstaller() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(mFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            Log.e(TAG, "package exist");
        } else {
            Log.e(TAG, "package does not exist");
        }
        // 隐藏的API
        try {
            Uri uri = Uri.fromFile(mFile);
            PackageInstallObserver observer = new PackageInstallObserver();
            packageManager.installPackage(uri, observer, PackageManager.INSTALL_REPLACE_EXISTING, packageInfo.packageName);
        } catch (Exception e) {
            e.printStackTrace();
            originPackageInstaller();
        }
    }

    /**
     * 需要framework.jar
     */
    class PackageInstallObserver extends IPackageInstallObserver.Stub implements IPackageInstallObserver {

        public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                if (packageInfo != null)
                    Log.e(TAG, "成功");
                else
                    Log.e(TAG, "失败");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void originPackageInstaller() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + mFile.getPath()), "application/vnd.android.package-archive");
        startActivityForResult(intent, 1);
    }

    private void systemPackageInstaller() {
        int resId = SilentInstaller.install(mFile.getPath(), MainActivity.this);
        Log.e(TAG, getResources().getString(resId));
    }

    private boolean checkPackagePermission(String permissionStr) {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionStr, getPackageName()));
        if (permission) {
            System.out.println(permissionStr + " : allow");
        } else {
            System.out.println(permissionStr + " : deny");
        }
        return permission;
    }

    private void startInstaller() {
        // 静默安装异常换系统安装，或者根据权限允许选用安装
        if (checkPackagePermission("android.permission.INSTALL_PACKAGES")) {
            StringBuilder command = new StringBuilder().append(" pm install -r ").append(mFile.getPath().replace(" ", "\\ "));
            ShellUtil.CommandResult result = ShellUtil.execRuntimeCommand(command.toString());
            // ShellUtil.CommandResult result = ShellUtil.execProceeBuilderCommand("pm", "install", "-r", mFile.getPath());

            Log.e(TAG, "result :" + result.result);
            Log.e(TAG, "msg : " + result.msg);
        } else {
            ShellUtil.CommandResult commandResult = ShellUtil.execProceeBuilderCommand("gsu", "shell", "pm", "install", "-r", mFile.getPath().replace(" ", "\\ "));
            if (!isInstallOK(commandResult)) {
                installNormal();
            }
        }
    }

    private boolean isInstallOK(ShellUtil.CommandResult result) {
        Log.e(TAG, "result :" + result.result);
        Log.e(TAG, "msg : " + result.msg);
        if (result.result != 0 || !result.msg.toLowerCase().contains("success")) {
            return false;
        }
        return true;
    }

    private void installNormal() {
        // String hideInstallAction = "android.intent.action.VIEW.HIDE";
        // String hideUninstallAction = "android.intent.action.DELETE.HIDE";
        // String installAction = Intent.ACTION_INSTALL_PACKAGE;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + mFile.getPath()), "application/vnd.android.package-archive");
        startActivityForResult(intent, 3);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "requestCode : " + requestCode);
        Log.e(TAG, "resultCode : " + resultCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(mPackageReceiver, intentFilter);
    }

    @Override
    public void finish() {
        unregisterReceiver(mPackageReceiver);
        super.finish();
    }

    private BroadcastReceiver mPackageReceiver = new BroadcastReceiver() {
        private String TAG = BroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(this.TAG, intent.getAction());
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                Toast.makeText(MainActivity.this, "安装成功", Toast.LENGTH_SHORT).show();
                if (mFile.exists()) {
                    mFile.delete();
                }
            }
        }
    };
}
