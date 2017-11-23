package com.gionee.gnvoiceassist.usecase;

import android.text.TextUtils;

import com.gionee.gnvoiceassist.customlink.CustomLinkSchema;
import com.gionee.gnvoiceassist.message.io.CustomInteractGenerator;
import com.gionee.gnvoiceassist.message.io.MetadataParser;
import com.gionee.gnvoiceassist.message.io.UsecaseResponseGenerator;
import com.gionee.gnvoiceassist.message.model.CUIEntity;
import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.message.model.UsecaseResponseEntity;
import com.gionee.gnvoiceassist.message.model.metadata.SmsSendMetadata;
import com.gionee.gnvoiceassist.usecase.annotation.CuiQuery;
import com.gionee.gnvoiceassist.usecase.annotation.CuiResult;
import com.gionee.gnvoiceassist.usecase.annotation.DirectiveResult;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants.UsecaseAlias;
import static com.gionee.gnvoiceassist.util.constants.ActionConstants.SmsAction.*;

/**
 * Created by liyingheng on 11/9/17.
 */

public class SmsSendUseCase extends UseCase {

    private static final String USECASE_ALIAS = "smssend";

    @Override
    public void handleMessage(DirectiveResponseEntity message) {
        String action = message.getAction();
        String subAction = message.getSubAction();
        SmsSendMetadata metadata = MetadataParser.toEntity(message.getMetadata(),SmsSendMetadata.class);
        switch (action) {
            case ACTION_REQUEST_SENDSMS:
                requestSendSms(metadata);
                break;
            case ACTION_CUI_MULTI_NUMBER:
                cuiMultiNumber(metadata);
                break;
            case ACTION_CUI_SIMSLOT:
                cuiSimslot(metadata);
                break;
            case ACTION_CUI_SENDSMS_CONFIRM:
                cuiSendConfirm(subAction,metadata);
                break;
        }
    }

    @Override
    public String getUseCaseName() {
        return UsecaseAlias.SMS;
    }

    @DirectiveResult
    private void requestSendSms(SmsSendMetadata metadata) {
        SmsSendOperation operation = new SmsSendOperation();
        boolean multiNumber = metadata.getDestination().size() > 1;
        boolean selectSimSlot = operation.isDualSimInserted() && TextUtils.isEmpty(metadata.getSimSlot());
        if (multiNumber) {
            queryMultiNumber(metadata);
        } else if (selectSimSlot) {
            querySimSlot(metadata);
        } else {
            querySendConfirm(metadata);
        }
    }

    @CuiResult
    private void cuiMultiNumber(SmsSendMetadata metadata) {
        requestSendSms(metadata);
    }

    @CuiResult
    private void cuiSimslot(SmsSendMetadata metadata) {
        requestSendSms(metadata);
    }

    @CuiResult
    private void cuiSendConfirm(String subAction, SmsSendMetadata metadata) {

    }

    @CuiQuery
    private void queryMultiNumber(SmsSendMetadata metadata) {
        CustomInteractGenerator generator = new CustomInteractGenerator(getUseCaseName(),ACTION_CUI_MULTI_NUMBER);
        for (int i = 0; i < metadata.getDestination().size(); i++) {
            String url = CustomLinkSchema.LINK_SMS +
                    "num=" + metadata.getDestination().get(i).getNumberList().get(0);
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
                .setRenderContent(null)
                .setSpeakText("你要选择哪个联系人？")
                .generateEntity();

        sendResponse(response);
    }

    @CuiQuery
    private void querySimSlot(SmsSendMetadata metadata) {
        String baseUrl = CustomLinkSchema.LINK_SMS + "num=" + metadata.getDestination().get(0);  //TODO 这里直接取首条条目有没有问题？会不会出现空的情况？
        CUIEntity customInteract = new CustomInteractGenerator(getUseCaseName(),ACTION_QUERY_SIMSLOT)
                .addCommand(baseUrl + "#sim=" + "1", "卡一", "卡伊")
                .addCommand(baseUrl + "#sim=" + "2","卡二","卡而","卡饿")
                .generateEntity();
        UsecaseResponseEntity response = new UsecaseResponseGenerator(getUseCaseName(),ACTION_QUERY_SIMSLOT)
                .setInCustomInteractive(true)
                .setCustomInteract(customInteract)
                .setMetadata(metadata.toJson())
                .setRenderContent(null)
                .setSpeakText("卡一还是卡二？")
                .generateEntity();
        sendResponse(response);
    }

    @CuiQuery
    private void querySendConfirm(SmsSendMetadata metadata) {

    }

    @Override
    public void onSpeakFinish(String utterId) {
        //TODO 发送短信确认后，开始发送短信
    }

    private class SmsSendOperation {

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
