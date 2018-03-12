package com.gionee.voiceassist.view.viewholder;

import android.content.Intent;
import android.provider.AlarmClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.ReminderCardEntity;

/**
 * @author twf
 * @date 2018/1/30
 */

public class ReminderCardViewHolder extends BaseViewHolder implements View.OnClickListener {

    private TextView triggerTimeView;
    private TextView triggerTimeCountDownView;
    private TextView reminderDateView;
    private TextView reminderDayView;
    private TextView reminderRepeatInfoView;
    private TextView reminderTitleView;
    private LinearLayout reminderTitleLayout;
    public ReminderCardViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ll_alarm_title) {
            Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
            if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void bind(CardEntity payload) {
        ReminderCardEntity reminderCardEntity = (ReminderCardEntity) payload;
        bindReminderData(reminderCardEntity);
    }

    private void initView() {
        reminderTitleView = (TextView) itemView.findViewById(R.id.reminder_title);
        triggerTimeView = (TextView) itemView.findViewById(R.id.tv_trigger_time);
        triggerTimeCountDownView = (TextView) itemView.findViewById(R.id.tv_trigger_countdown_info);
        reminderDateView = (TextView) itemView.findViewById(R.id.tv_alarm_date);
        reminderDayView = (TextView) itemView.findViewById(R.id.tv_alarm_day);
        reminderRepeatInfoView = (TextView) itemView.findViewById(R.id.tv_alarm_repeat_info);
        reminderTitleLayout = (LinearLayout) itemView.findViewById(R.id.ll_alarm_title);
        reminderTitleLayout.setOnClickListener(this);
    }

    private void bindReminderData(ReminderCardEntity reminderCardEntity) {
        reminderTitleView.setText(reminderCardEntity.getReminderContent());
        triggerTimeView.setText(reminderCardEntity.getReminderHour());
        triggerTimeCountDownView.setText(reminderCardEntity.getTriggerCountDownInfo());
        reminderDateView.setText(reminderCardEntity.getReminderDateStr());
        reminderDayView.setText(reminderCardEntity.getReminderDayOfWeek());

        if(TextUtils.isEmpty(reminderCardEntity.getRepeatInfo())) {
            reminderRepeatInfoView.setVisibility(View.GONE);
        } else {
            reminderRepeatInfoView.setVisibility(View.VISIBLE);
            reminderRepeatInfoView.setText(reminderCardEntity.getRepeatInfo());
        }
    }

    public static ReminderCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_add_alarm, parent, false);
        return new ReminderCardViewHolder(itemView);
    }
}
