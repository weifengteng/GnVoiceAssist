package com.gionee.gnvoiceassist.usecase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.message.io.CustomInteractGenerator;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.io.UsecaseResponseGenerator;
import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.PhonecallMetadata;
import com.gionee.gnvoiceassist.usecase.annotation.CuiQuery;
import com.gionee.gnvoiceassist.usecase.annotation.DirectiveResult;
import com.gionee.gnvoiceassist.usecase.annotation.Operation;
import com.gionee.gnvoiceassist.util.Preconditions;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants.UsecaseAlias;

import static com.gionee.gnvoiceassist.util.constants.ActionConstants.PhonecallAction.*;


/**
 * Created by liyingheng on 11/9/17.
 */

public class PhonecallUseCase extends UseCase {

    private static final String USECASE_ALIAS = UsecaseAlias.PHONECALL;
    private PhonecallMetadata mTempMetadata;
    private Context mAppCtx;

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        String action = message.getAction();
        String subAction = message.getSubAction();
        PhonecallMetadata metadata = MetadataParser.toEntity(message.getMetadata(), PhonecallMetadata.class);
        switch (action) {
            case ACTION_REQUEST_CALL:
                requestCall(metadata);
                break;
            case ACTION_CUI_SIMSLOT:
                cuiSelectSim(metadata);
                break;
            case ACTION_CUI_MULTI_NUMBER:
                cuiMultiNumber(metadata);
                break;
        }
    }

    @Override
    public String getUseCaseName() {
        return UsecaseAlias.PHONECALL;
    }

    @DirectiveResult
    private void requestCall(PhonecallMetadata metadata) {
        // 1.判断是否有多个号码
        // 2.判断是否需多个卡
        PhonecallOperation phonecallOperation = new PhonecallOperation();
        boolean multiEntries = (metadata.getContacts().size() > 1
                || (metadata.getContacts().size() != 0 && metadata.getContacts().get(0).getNumberList().size() > 1));
        boolean selectSim = phonecallOperation.isDualSimInserted() && TextUtils.isEmpty(metadata.getSimSlot());

        if (multiEntries) {
            //发起选择号码多轮交互
            queryMultiNumber(metadata);
        } else if (selectSim) {
            //发起选择卡槽多轮交互
            querySelectSim(metadata);
        } else {
            readyToMakeCall(metadata);
            //打电话
        }
    }

    private void cuiMultiNumber(PhonecallMetadata metadata) {
        requestCall(metadata);
    }

    private void cuiSelectSim(PhonecallMetadata metadata) {
        requestCall(metadata);
    }

    private void readyToMakeCall(PhonecallMetadata metadata) {
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(), ACTION_RESULT_CALL)
                .setInCustomInteractive(true)
                .setMetadata(metadata.toJson())
                .setSpeakText("正在拨打电话")
                .generateEntity();
        sendResponse(response, this);
        tempSaveMetadata(metadata);
    }

    @Operation
    private void makeCall(PhonecallMetadata metadata) {
        PhonecallOperation operation = new PhonecallOperation();
        if (TextUtils.isEmpty(metadata.getSimSlot())) {
            if (operation.isDualSimAvailable()) {
                //此时为单卡情况。需要将手机所用的卡槽读取出来
                metadata.setSimSlot(operation.getCurrentSim());
            } else {
                metadata.setSimSlot("0");
            }
        }
        operation.call(metadata.getContacts().get(0).getNumberList().get(0), metadata.getSimSlot());
    }

    /**
     * 临时存储Metadata数据到Usecase全局
     * @param metadata
     */
    private void tempSaveMetadata(PhonecallMetadata metadata) {
        mTempMetadata = metadata;
    }

    /**
     * 清除临时存储的Metadata
     * @param metadata
     */
    private void clearTempMetadata(PhonecallMetadata metadata) {
        mTempMetadata = null;
    }

    @CuiQuery("multi_number")
    private void queryMultiNumber(PhonecallMetadata metadata) {
        CustomInteractGenerator generator = new CustomInteractGenerator(getUseCaseName(), ACTION_QUERY_MULTI_NUMBER);
        for (int i = 0; i < metadata.getContacts().size(); i++) {
            String url = CustomLinkSchema.LINK_PHONE +
                    "num=" + metadata.getContacts().get(i).getNumberList().get(0);
            if (!TextUtils.isEmpty(metadata.getSimSlot())) {
                url += "#" + "sim=" + metadata.getSimSlot();
            }
            if (!TextUtils.isEmpty(metadata.getSimSlot())) {
                url += "#" + "carrier=" + metadata.getCarrier();
            }
            generator.addCommand(url, String.valueOf(i + 1), "第" + (i + 1), "第" + (i + 1) + "条");
        }
        CUIEntity customInteract = generator.generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(), ACTION_QUERY_MULTI_NUMBER)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setSpeakText("您要选择哪一个号码？")
                .setRenderContent(null) //TODO Render Content
                .generateEntity();

        sendResponse(response);
    }

    @CuiQuery("select_sim")
    private void querySelectSim(PhonecallMetadata metadata) {
        String baseUrl = CustomLinkSchema.LINK_PHONE + "num=" + metadata.getContacts().get(0).getNumberList().get(0);  //TODO 这里直接取首条条目有没有问题？会不会出现空的情况？
        CUIEntity customInteract = new CustomInteractGenerator(getUseCaseName(), ACTION_QUERY_SIMSLOT)
                .addCommand(baseUrl + "#sim=" + "1", "卡一", "卡伊")
                .addCommand(baseUrl + "#sim=" + "2", "卡二", "卡而", "卡饿")
                .generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(), ACTION_QUERY_SIMSLOT)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setRenderContent(null)
                .generateEntity();
        sendResponse(response);
    }

    @Override
    public void onSpeakFinish(String utterId) {
        //TODO  加入拨打电话TTS播报的utterance
        makeCall(Preconditions.checkNotNull(mTempMetadata));
    }

    //TODO 此内部类将来可能移动到devicecontrol包中，因为此操作属于系统底层控制
    private class PhonecallOperation {
        /**
         * 拨打电话
         * @param tel 电话号码
         */
        private void call(String tel, String simslot) {
            //TODO 实现打电话功能
            if (mAppCtx == null) {
                mAppCtx = GnVoiceAssistApplication.getInstance().getApplicationContext();
            }
            if (!TextUtils.isEmpty(tel) && mAppCtx != null) {
                Uri uri = Uri.parse("tel:" + tel);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(uri);
                if (intent.resolveActivity(mAppCtx.getPackageManager()) != null) {
                    // TODO: 实现选卡打电话功能
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mAppCtx.startActivity(intent);
                }
            }
        }

        /**
         * 当前设备是否支持双卡
         * @return true支持双卡，false不支持双卡
         */
        private boolean isDualSimAvailable() {
            return false;
        }

        /**
         * 当前设备是否插入了双卡
         * @return true双卡插入，false双卡没插入
         */
        private boolean isDualSimInserted() {
            return false;
        }

        private String getCurrentSim() {
            //TODO 查询当前选卡状态
            return "0";
        }
    }
}
