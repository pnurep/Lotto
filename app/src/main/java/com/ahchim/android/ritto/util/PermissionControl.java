package com.ahchim.android.ritto.util;

/**
 * Created by Gold on 2017. 4. 1..
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 권한처리를 담당하는 클래스
 *
 * 권한변경시 PERMISSION_ARRAY의 값만 변경해주면 된다
 *
 */

public class PermissionControl {
    // 요청할 권한 목록
    public static final String PERMISSION_ARRAY[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
    };

    // 권한체크 함수
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity activity, int req_permission){
        // 1.1 런타임 권한체크
        // 위에 설정한 권한을 반복문을 돌면서 처리한다...
        boolean permCheck = true;
        for(String perm : PERMISSION_ARRAY){
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                permCheck = false;
                break;
            }
        }
        // 1.2 퍼미션이 모두 true 이면 그냥 프로그램 실행
        if( permCheck ){
            return true;
        }else{
            activity.requestPermissions(PERMISSION_ARRAY, req_permission);
            return false;
        }
    }

    // 권한체크 후 콜백처리
    public static boolean onCheckResult(int[] grantResults) {
        boolean checkResult = true;
        // 권한처리 결과값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다
        for(int result : grantResults) {
            if(result != PackageManager.PERMISSION_GRANTED){
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }
}
