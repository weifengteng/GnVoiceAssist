package com.gionee.voiceassist.view.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.AlarmCardEntity;
import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * @author twf
 * @date 2018/1/26
 */

public class AlarmCardViewHolder extends BaseViewHolder implements View.OnClickListener{
    private TextView triggerTimeView;
    private TextView triggerTimeCountDownView;
    private TextView alarmDateView;
    private TextView alarmDayView;
    private TextView alarmRepeatInfoView;
    private LinearLayout alarmTitleLayout;

    private AlarmCardViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    @Override
    public void bind(CardEntity payload) {
        AlarmCardEntity alarmCardEntity = new AlarmCardEntity();
        bindAlarmData(alarmCardEntity);
    }

    private void initView() {
        triggerTimeView = (TextView) itemView.findViewById(R.id.tv_trigger_time);
        triggerTimeCountDownView = (TextView) itemView.findViewById(R.id.tv_trigger_countdown_info);
        alarmDateView = (TextView) itemView.findViewById(R.id.tv_alarm_date);
        alarmDayView = (TextView) itemView.findViewById(R.id.tv_alarm_day);
        alarmRepeatInfoView = (TextView) itemView.findViewById(R.id.tv_alarm_repeat_info);
        alarmTitleLayout = (LinearLayout) itemView.findViewById(R.id.ll_alarm_title);
        alarmTitleLayout.setOnClickListener(this);
    }

    private void bindAlarmData(AlarmCardEntity alarmCardEntity) {
        triggerTimeView.setText(alarmCardEntity.getAlarmContent());
        triggerTimeCountDownView.setText(alarmCardEntity.getTriggerCountDownInfo());
        alarmDateView.setText(alarmCardEntity.getAlarmDate());
        alarmDayView.setText(alarmCardEntity.getAlarmDay());

        if(TextUtils.isEmpty(alarmCardEntity.getRepeatInfo())) {
            alarmRepeatInfoView.setVisibility(View.GONE);
        } else {
            alarmRepeatInfoView.setVisibility(View.VISIBLE);
            alarmRepeatInfoView.setText(alarmCardEntity.getRepeatInfo());
        }
    }

    public static AlarmCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_add_alarm, parent, false);
        return new AlarmCardViewHolder(itemView);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ll_alarm_title) {
            //TODO: go to alarm activity
        }
    }
}
