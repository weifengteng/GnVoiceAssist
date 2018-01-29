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

import java.util.List;

/**
 * 快捷开关ViewHolder
 */

public class QuickSettingsViewHolder extends BaseViewHolder {

    //actionFeedback的URL格式： quicksetting://[settingAlias]/[enable]
    //action点击URL：          quicksetting://[settingAlias]/clicked
    private static final String QUICK_SETTING_SCHEME = "quicksetting";
    private static final String CLICK_ACTIONBAR_URLPATH = "clicked";

    private View settingTitleBar;
    private TextView tvActionName;
    private ImageView ivActionIcon;
    private LinearLayout lytOptions;
    private View mView;

    public QuickSettingsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        settingTitleBar = itemView.findViewById(R.id.action_title_bar);
        tvActionName = (TextView) itemView.findViewById(R.id.tv_action_name);
        ivActionIcon = (ImageView) itemView.findViewById(R.id.iv_action_icon);
        lytOptions = (LinearLayout) itemView.findViewById(R.id.lyt_content);
    }

    @Override
    public void bind(CardEntity payload) {
        QuickSettingCardEntity qsPayload = (QuickSettingCardEntity) payload;
        bindActionTitleBar(qsPayload);
        bindOptions(qsPayload);
    }

    public static QuickSettingsViewHolder newInstance (ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_quicksettingscard_lyt, parent, false);
        return new QuickSettingsViewHolder(itemView);
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
            View nodeView = LayoutInflater.from(
                    lytOptions.getContext()).inflate(R.layout.card_item_quicksettingscard,
                    lytOptions,
                    false);
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
                    String feedbackUrl = QUICK_SETTING_SCHEME + "://" + node.optionAlias + "/" + (isChecked ? "enable":"disable");
                    DataController.getDataController().getScreenController().uiActionFeedback(feedbackUrl);
                }
            });

        }
    }

    private void setText(TextView view, String content) {
        boolean hasContent = !TextUtils.isEmpty(content);
        view.setVisibility(hasContent ? View.VISIBLE:View.GONE);
        view.setText(content);
    }

}
