package freestar.rvtest;

import com.jaydenxiao.common.baseapp.BaseApplication;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
//        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        init();
    }

    private void init() {
        // initialize最好放在attachBaseContext最前面，初始化直接在Application类里面，切勿封装到其他类
        SophixManager.getInstance().setContext(this)
                .setAppVersion("20170916")
                .setAesKey(null)
                .setSecretMetaData("24625539-1", "61eef2d31c7f560c7c70e5a17b1067bd",
                        "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCu8O6AoiS46mHmUPBnTlgRLvLfR3mM9L7YodQ1+sTWI6sejbAlhywEYGAOIAgYsnVrCwy+5VQnXmsitlW/VlcUZAXuPrD86lHK0UtN2si+NLsZ6UnFEDTsysvltNMmO77F7ktwnF1g25sAyW7pdfpmvmsFTWa2vowc7RpG24C1oHdZS3GQYqiHrNeLf2KOmdP80QtK4ePf1A65ggggQE8jXJTbC3PqbG4/PbenYebAXjYnRBhBQKpULyobWFB/ymSWwis4Ebuow8uf8PnoaXrMU5ThN89k3ru46Z3W0GftyfMrjcyfm1u8X65lcWtdniK6uAlZ1/ueymhQT1/mXsV/AgMBAAECggEATmH9Ov1rXr8FpmBVlrPvmaNjhs9LkvCGIiJAJv94+kl4/nOVnPr3qe5Um3+WoRo0qgWFbHLsRcd0UVVJDjXtRmGalqjYPDnw8+LazsNcdnzB6FYyi207iW1D9mQSx2q1i5G9k3M8E43R4YRfgZMu4qIm6XVItFCkDsHdqCnpfXbuSZEfbbMc2nslqOQm6/Z2Ocq7La3NgQUHVKfXErbtCU0YljEZZngoKVjIgPeaC5wXlBwg0QcZaxTo05laDXC/aLxNhM6uNuVV0WIVGd8oEZ8XSBCMNU2uU5fXWwzlip4dJkh/6bENJDslMscxEVrl198GrVdwLZyJgbXfssSeQQKBgQDk4Pvof2KmfU1RYYkV+K5k2nQuGpfaoH/mqWqXhO+V+bvpMjqqW+a7ZufCk4WoOolyNnhtR+uxyV+EFdbl2ydJL8wlvFFlLBk5XIH1q9chSLJLR7wUNvKvXuYHD20l6COHfxlxWJQo0PS8TivOslLPcHyQRKg0X+dDGjw9OotlOQKBgQDDq8DCF+rjRNrDQqvHKXQmj+OsVBhHrf4Ni4NWkccVS0vGKzl6gRUtMzSjSTbr84WTo+drHLGJUHvZssTGrrnNPVxIf7mdruMGHxjk9M0yn9lssFOONmEwS1BRZFjlQ92pL33SoJuFux3dXBtq3cFXYXwayLFIMgxEk7aSOSB4dwKBgQDdEst/aPuaoNE72CAXjtq0I6WwmpMu43RnEiEZNZhNnqj4uEGeJ6KYRO97LA8fzVrRbPepZpysXzvZU1b9Mk+iC37+vAVK2euLUmVdLJxhU9yThjiOtfx3lAxPqYDd6nnW+NdUeiCxxBOAh2kc/3WpVJecqhRmcnPohsDEy3W1UQKBgE3IRMPjRhg6Esx0AF0XeXiq0lJ6y3wTCl7QCtGq+XzZhbGtFRhBn+1r4YzXWeHJ2FusWB4YNerj95e+nMJuSqXvsnOPcnXgQ5b3XaoGNlvdd0zhexWZQGE2TyNLxJq+s6rBmj7UYX5zt5d7OdrtdCIMOo1bIj4UxxOrtHPECchZAoGAY3r/xrC8/eb+gPtyFCuuyQds58Ct3wKEpOI8dzzaNyhei7hD/uew+E1RCYmSE4wLynLAD0DNUadiHjKJ663OkRhJofltb1iH0urSTmTKNmEKMFVS2Y3d1ZsNcMHnOWBQuE4j+tc3YG2pNa1zSSdrDGxfUfhJfHJ8VCXvK7lpfMk=\n" +
                                "确认")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Logger.e("表明补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            Logger.e("表明新补丁生效需要重启");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Logger.e("其他错误");
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
