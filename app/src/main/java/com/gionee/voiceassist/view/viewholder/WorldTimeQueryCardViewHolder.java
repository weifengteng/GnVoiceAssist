package com.gionee.voiceassist.view.viewholder;

import android.content.Intent;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.WorldTimeQueryCardEntity;

/**
 * 查询世界时显示的卡片
 * @author twf
 * @date 2018/3/13
 */

public class WorldTimeQueryCardViewHolder extends BaseViewHolder implements View.OnClickListener {
    private LinearLayout mWorldTimeQueryTitleLayout;
    private TextView mDateQueryTextView;
    private TextView mTimeZoneCityTextView;
    private TextView mDiffTimeTextView;
    private TextView mHourMinuteTextView;

    public WorldTimeQueryCardViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void bind(CardEntity payload) {
        WorldTimeQueryCardEntity entity = (WorldTimeQueryCardEntity) payload;
        bindData(entity);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ll_world_time_query_title) {
            Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
            if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(intent);
            }
        }
    }

    private void initView(View view) {
        mWorldTimeQueryTitleLayout = (LinearLayout) view.findViewById(R.id.ll_world_time_query_title);
        mDateQueryTextView = (TextView) view.findViewById(R.id.date_query_time);
        mTimeZoneCityTextView = (TextView) view.findViewById(R.id.city_timezone);
        mDiffTimeTextView = (TextView) view.findViewById(R.id.time_diff_timezones);
        mHourMinuteTextView = (TextView) view.findViewById(R.id.hour_minute_timezone);

        mWorldTimeQueryTitleLayout.setOnClickListener(this);
    }

    private void bindData(WorldTimeQueryCardEntity entity) {
        mDateQueryTextView.setText(entity.getQueryDate());
        mTimeZoneCityTextView.setText(entity.getTimeZoneCity());
        mDiffTimeTextView.setText(entity.getTimeDiff());
        mHourMinuteTextView.setText(entity.getHourAndMinute());
    }

    public static WorldTimeQueryCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item_query_world_time, parent, false);
        return new WorldTimeQueryCardViewHolder(view);
    }
}
