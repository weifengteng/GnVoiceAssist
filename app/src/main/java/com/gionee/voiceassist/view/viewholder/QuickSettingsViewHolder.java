package com.gionee.voiceassist.view.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.controller.appcontrol.DataController;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.QuickSettingCardEntity;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.view.adapter.DialogSubItemPool;

import java.util.List;

/**
 * 快捷开关ViewHolder
 */

public class QuickSettingsViewHolder extends BaseViewHolder {

    private static final String TAG = QuickSettingsViewHolder.class.getSimpleName();

    //actionFeedback的URL格式： quicksetting://[settingAlias]/[enable]
    //action点击URL：          quicksetting://[settingAlias]/clicked
    private static final String QUICK_SETTING_SCHEME = "quicksetting";
    private static final String CLICK_ACTIONBAR_URLPATH = "clicked";

    private View settingTitleBar;
    private TextView tvActionName;
    private ImageView ivActionIcon;
    private LinearLayout lytOptions;
    private View mView;
    private DialogSubItemPool mItemPool;
    private QuickSettingCardEntity mQsPayload;
    private QuickSettingCardEntity.QuickSettingObserver mObserver = new QuickSettingCardEntity.QuickSettingObserver() {
        @Override
        public void onStateChanged(String optionAlias, QuickSettingCardEntity.QuickSettingState state) {
            LogUtil.d(TAG, "QuickSettingCardObserver onStateChanged. OptionAlias:" + optionAlias + "; state:" + state);
            updateOptionState(optionAlias, state);
        }

        @Override
        public void onDescriptionChanged(String optionAlias, String description) {
            LogUtil.d(TAG, "QuickSettingCardObserver onDescriptionChanged. OptionAlias:" + optionAlias + "; Description:" + description);
            updateOptionDescription(optionAlias, description);
        }
    };

    public QuickSettingsViewHolder(View itemView, DialogSubItemPool itemPool) {
        super(itemView);
        mItemPool = itemPool;
        mView = itemView;
        settingTitleBar = itemView.findViewById(R.id.action_title_bar);
        tvActionName = (TextView) itemView.findViewById(R.id.tv_action_name);
        ivActionIcon = (ImageView) itemView.findViewById(R.id.iv_action_icon);
        lytOptions = (LinearLayout) itemView.findViewById(R.id.lyt_content);
    }

    @Override
    public void bind(CardEntity payload) {
        mQsPayload = (QuickSettingCardEntity) payload;
        mQsPayload.addObserver(mObserver);
        bindActionTitleBar(mQsPayload);
        bindOptions(mQsPayload);
    }

    public static QuickSettingsViewHolder newInstance (ViewGroup parent, DialogSubItemPool itemPool) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_quicksettingscard_lyt, parent, false);
        return new QuickSettingsViewHolder(itemView, itemPool);
    }

    private void bindActionTitleBar(final QuickSettingCardEntity payload) {
        tvActionName.setText(payload.getSettingName());
        if (payload.getSettingIcon() != -1) {
            ivActionIcon.setVisibility(View.VISIBLE);
            ivActionIcon.setImageResource(payload.getSettingIcon());
        } else {
            ivActionIcon.setVisibility(View.GONE);
//            ivActionIcon.setImageDrawable(null);
        }
        settingTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackUrl = QUICK_SETTING_SCHEME + "://" + payload.getSettingAlias() + "/" + CLICK_ACTIONBAR_URLPATH;
                DataController.getDataController().getScreenController().uiActionFeedback(feedbackUrl);
            }
        });
    }

    private void bindOptions(QuickSettingCardEntity payload) {
        List<QuickSettingCardEntity.QuickSettingItem> parentNodes = payload.getOptionNodes();
        bindNodes(parentNodes);
    }

    private void bindNodes(List<QuickSettingCardEntity.QuickSettingItem> nodes) {
        for (final QuickSettingCardEntity.QuickSettingItem node:nodes) {
            View nodeView = mItemPool.getQuickSettingsItemView(lytOptions);
            nodeView.setTag(node.optionAlias);
            TextView tvOptionName = (TextView) nodeView.findViewById(R.id.tv_option_name);
            TextView tvOptionDescription = (TextView) nodeView.findViewById(R.id.tv_option_description);
            Switch optionSwitch = (Switch) nodeView.findViewById(R.id.switch_control);

            setText(tvOptionName, node.optionName);
            setText(tvOptionDescription, node.optionDescription);
            optionSwitch.setChecked(node.optionState == QuickSettingCardEntity.QuickSettingState.ENABLED);

            lytOptions.addView(nodeView);
            bindNodes(node.getSubOptionNodes());

            optionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        buttonView.setEnabled(false);
                        String feedbackUrl = QUICK_SETTING_SCHEME + "://" + node.optionAlias + "/" + (isChecked ? "enable":"disable");
                        DataController.getDataController().getScreenController().uiActionFeedback(feedbackUrl);
                    }
                }
            });

        }
    }

    private void updateOptionState(String optionAlias, QuickSettingCardEntity.QuickSettingState state) {
        View nodeView = lytOptions.findViewWithTag(optionAlias);
        if (nodeView != null) {
            Switch controlSwitch = (Switch)nodeView.findViewById(R.id.switch_control);
            controlSwitch.setEnabled(true);
            controlSwitch.setChecked(state == QuickSettingCardEntity.QuickSettingState.ENABLED);
        }
    }

    private void updateOptionDescription(String optionAlias, String optionDesc) {
        View nodeView = lytOptions.findViewWithTag(optionAlias);
        if (nodeView != null) {
            TextView tvDesc = (TextView) nodeView.findViewById(R.id.tv_option_description);
            tvDesc.setText(optionDesc);
        }
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        if (mQsPayload != null) {
            mQsPayload.removeObserver(mObserver);
        }
        recycleItemView();
    }

    private void recycleItemView() {
        int childCount = lytOptions.getChildCount();
        LogUtil.d(TAG, "recycleItemView. Container initial size = " + childCount);
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = lytOptions.getChildAt(i);
            mItemPool.recycleQuickSettingsItemView(childView);
            lytOptions.removeView(childView);
        }
        LogUtil.d(TAG, "recycleItemView. Item recycled. Container size= " + lytOptions.getChildCount());
    }

    private void setText(TextView view, String content) {
        boolean hasContent = !TextUtils.isEmpty(content);
        view.setVisibility(hasContent ? View.VISIBLE:View.GONE);
        view.setText(content);
    }

}
