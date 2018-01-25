package com.gionee.voiceassist.view.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.QuickSettingCardEntity;

/**
 * Created by liyingheng on 1/24/18.
 */

public class QuickSettingsViewHolder extends BaseViewHolder {

    private View actionTitleBar;
    private TextView tvActionName;
    private ImageView ivActionIcon;
    private TextView tvControlState;
    private Switch switchControl;

    public QuickSettingsViewHolder(View itemView) {
        super(itemView);
        actionTitleBar = itemView.findViewById(R.id.action_title_bar);
        tvActionName = (TextView) itemView.findViewById(R.id.tv_action_name);
        ivActionIcon = (ImageView) itemView.findViewById(R.id.iv_action_icon);
        tvControlState = (TextView) itemView.findViewById(R.id.tv_state);
        switchControl = (Switch) itemView.findViewById(R.id.switch_control);
    }

    @Override
    public void bind(CardEntity payload) {
        QuickSettingCardEntity qsPayload = (QuickSettingCardEntity) payload;
        CardEntity.AppAction appAction = qsPayload.getOpenAppAction();
        tvActionName.setText(appAction.name);

    }

    public static QuickSettingsViewHolder newInstance (ViewGroup parent) {
        return null;
    }
}
