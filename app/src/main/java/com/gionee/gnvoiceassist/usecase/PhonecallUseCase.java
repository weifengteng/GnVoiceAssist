package com.gionee.gnvoiceassist.usecase;

import android.text.TextUtils;

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

/**
 * Created by liyingheng on 11/9/17.
 */

public class PhonecallUseCase extends UseCase {

    private static final String USECASE_ALIAS = "phonecall";
    //传入的action
    public static final String ACTION_REQUEST_CALL = "request_call";
    public static final String ACTION_CUI_SIMSLOT = "cui_simslot";
    public static final String ACTION_CUI_MULTI_NUMBER = "cui_multi_number";
    //发出的action
    public static final String ACTION_QUERY_SIMSLOT = "query_simslot";
    public static final String ACTION_QUERY_MULTI_NUMBER = "query_multi_number";
    public static final String ACTION_RESULT_CALL = "result_call";

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        String action = message.getAction();
        String subAction = message.getSubAction();
        PhonecallMetadata metadata = MetadataParser.toEntity(message.getMetadata(),PhonecallMetadata.class);
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
        return USECASE_ALIAS;
    }

    @DirectiveResult
    private void requestCall(PhonecallMetadata metadata) {
        // 1.判断是否有多个号码
        // 2.判断是否需多个卡
        PhonecallOperation phonecallOperation = new PhonecallOperation();
        boolean multiEntries = metadata.getNumber().length > 1;
        boolean selectSim = phonecallOperation.isDualSimInserted() & TextUtils.isEmpty(metadata.getSimSlot());

        if (multiEntries) {
            //发起选择号码多轮交互
            queryMultiNumber(metadata);
        } else if (selectSim) {
            //发起选择卡槽多轮交互
            querySelectSim(metadata);
        } else {
            makeCall(metadata);
            //打电话
        }
    }

    private void cuiMultiNumber(PhonecallMetadata metadata) {
        requestCall(metadata);
    }

    private void cuiSelectSim(PhonecallMetadata metadata) {
        requestCall(metadata);
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
        operation.call(metadata.getNumber()[0],metadata.getSimSlot());
    }

    @CuiQuery("multi_number")
    private void queryMultiNumber(PhonecallMetadata metadata) {
        CustomInteractGenerator generator = new CustomInteractGenerator(ACTION_CUI_MULTI_NUMBER);
        for (int i = 0; i < metadata.getNumber().length; i++) {
            String url = CustomLinkSchema.LINK_PHONE +
                    "num=" + metadata.getNumber()[i];
            if (!TextUtils.isEmpty(metadata.getSimSlot())) {
                url += "#" + "sim=" + metadata.getSimSlot();
            }
            if (!TextUtils.isEmpty(metadata.getSimSlot())) {
                url += "#" + "carrier=" + metadata.getCarrier();
            }
            generator.addCommand(url,String.valueOf(i + 1),"第" + (i+1), "第" + (i+1) + "条");
        }
        CUIEntity customInteract = generator.generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(),ACTION_QUERY_MULTI_NUMBER)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setShouldRender(true)
                .setShouldSpeak(true)
                .setRenderContent(null)
                .generateEntity();

        sendResponse(response);
    }

    @CuiQuery("select_sim")
    private void querySelectSim(PhonecallMetadata metadata) {
        String baseUrl = CustomLinkSchema.LINK_PHONE + "num=" + metadata.getNumber()[0];
        CUIEntity customInteract = new CustomInteractGenerator(ACTION_QUERY_SIMSLOT)
                .addCommand(baseUrl + "#sim=" + "1", "卡一", "卡伊")
                .addCommand(baseUrl + "#sim=" + "2","卡二","卡而","卡饿")
                .generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(),ACTION_QUERY_SIMSLOT)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setShouldRender(true)
                .setRenderContent(null)
                .generateEntity();
        sendResponse(response);
    }

    //TODO 此内部类将来可能移动到devicecontrol包中，因为此操作属于系统底层控制
    private class PhonecallOperation {
        /**
         * 拨打电话
         * @param tel 电话号码
         */
        private void call(String tel, String simslot) {

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
