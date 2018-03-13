package com.gionee.voiceassist.view.viewholder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.DateQueryCardEntity;

/**
 * @author twf
 * @date 2018/3/12
 */

public class DateQueryCardViewHolder extends BaseViewHolder implements View.OnClickListener {

    private LinearLayout mDateTitleLayout;
    private TextView mFestivalTextView;
    private TextView mLunarTextView;
    private TextView mDayAndMonthTextView;
    private TextView mYearAndWeekTextView;

    public DateQueryCardViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ll_date_title) {
            launchCalendar(view.getContext());
        }
    }

    @Override
    public void bind(CardEntity payload) {
        DateQueryCardEntity dateQueryCardEntity = (DateQueryCardEntity) payload;
        bindDateData(dateQueryCardEntity);
    }

    public static DateQueryCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.card_item_query_date, parent, false);
        return new DateQueryCardViewHolder(itemView);
    }

    private void initView(View item) {
        mDateTitleLayout = (LinearLayout) item.findViewById(R.id.ll_date_title);
        mFestivalTextView = (TextView) item.findViewById(R.id.festival);
        mLunarTextView = (TextView) item.findViewById(R.id.lunar_date);
        mDayAndMonthTextView = (TextView) item.findViewById(R.id.day_and_month);
        mYearAndWeekTextView = (TextView) item.findViewById(R.id.year_and_week);

        mDateTitleLayout.setOnClickListener(this);
    }

    private void bindDateData(DateQueryCardEntity entity) {
        mFestivalTextView.setText(entity.getDateFestival());
        mLunarTextView.setText(entity.getLunarDate());
        mDayAndMonthTextView.setText(entity.getMonthAndDay());
        mYearAndWeekTextView.setText(entity.getYearAndWeek());
    }

    private void launchCalendar(Context context) {
        Intent i = new Intent();
        ComponentName cn;
        if(Integer.parseInt (Build.VERSION.SDK ) >= 8){
            cn = new ComponentName("com.android.calendar","com.android.calendar.LaunchActivity");
        } else {
            cn = new ComponentName("com.google.android.calendar","com.android.calendar.LaunchActivity");
        }
        i.setComponent(cn);
        if (i.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(i);
        }
    }
}
