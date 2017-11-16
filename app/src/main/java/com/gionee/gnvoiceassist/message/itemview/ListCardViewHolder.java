package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.ListRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnvoiceassist.R.id.tel;

/**
 * Created by liyingheng on 11/16/17.
 */

public class ListCardViewHolder extends BaseViewHolder {

    private static final String TAG = ListCardViewHolder.class.getSimpleName();
    LinearLayout mCustomLayout;

    public ListCardViewHolder(View itemView) {
        super(itemView);
        mCustomLayout = (LinearLayout) itemView.findViewById(R.id.custom_panel);
    }

    @Override
    public void bind(RenderEntity data, Context context) {
        ListRenderEntity renderData = (ListRenderEntity) data;
        for (RenderEntity.ListItemModel item : renderData.getList()) {
            View itemView = View.inflate(context,R.layout.listcard_info_item_lyt, null);
            if (item == null) {
                mCustomLayout.addView(itemView);
                continue;
            }

            setTitleTextView(item.title, itemView);
//    		setScoreImage(itemView, new String[] { "75" });
            setContentView(item.content, itemView, context);
            setDetailUrlView(item.url, itemView);
            if (item.image != null) {
                setImage(item.image, itemView, context);
            }
            mCustomLayout.addView(itemView);

        }
    }

    public static ListCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.empty_note_board, parent,false);
        return new ListCardViewHolder(itemView);
    }

    private void setTitleTextView(String title, View itemView) {
        if(title != null) {
            LogUtil.d(TAG, "SimpleListCardItem setTitleTextView name = " + title);
            ((TextView) itemView.findViewById(R.id.title)).setText(title);
        } else {
            LogUtil.d(TAG, "SimpleListCardItem setTitleTextView name == null");
        }
    }

    private void setContentView(String content, View itemView, Context context) {

        if(TextUtils.isEmpty(content)) {
            LogUtil.e(TAG, "SimpleListCardItem setContentView content is empty");
            return;
        }

        String[] newTels = new String[]{content};

        List<String> list = new ArrayList<String>();
        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(tel);
        for(final String tel : newTels) {
            list.add(tel);
            View telView = View.inflate(context,R.layout.phone_number_call_item, null);
            TextView contentView = (TextView) telView.findViewById(R.id.number);
//			contentView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            contentView.setText(tel);
			/*contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					telephoneByNum(tel);
				}
			});*/

            linearLayout.addView(telView);
        }
        LogUtil.d(TAG, "SimpleListCardItem setContentView list = " + list);
    }

    private void setImage(RenderEntity.ImageModel image, View itemView, Context context) {
        if(image == null || TextUtils.isEmpty(image.src)) {
            LogUtil.e(TAG, "setImage() imageSrc is null or empty");
            return;
        }

        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.image);
        imageButton.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(image.src)
                .placeholder(R.drawable.gn_detail_item_icon_phone_normal)
                .resize(240, 160)
                .onlyScaleDown()
                .into(imageButton);
    }

    private void setDetailUrlView(final String detailUrl, View itemView) {
        if(detailUrl != null) {
            LogUtil.d(TAG, "SimpleListCardItem setDetailUrlView addr[0] = " + detailUrl);
            TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
            detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            detailUrlView.setText("更多信息");
            detailUrlView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    openLink(detailUrl);
                }
            });


        } else {
            LogUtil.e(TAG, "SimpleListCardItem setDetailUrlView addr == null");
        }
    }

    private void setScoreImage(View itemView, String[] score) {
        ImageView scoreImage = ((ImageView) itemView.findViewById(R.id.score));
        if(score != null) {
            int scoreValue = Integer.parseInt(score[0]);
            LogUtil.d(TAG, "SimpleListCardItem setScoreImage scoreValue = " + scoreValue);
            if(scoreValue <= 0) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_0);
            } else if(scoreValue <= 25) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_1);
            } else if(scoreValue <= 50) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_2);
            } else if(scoreValue <= 75) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_3);
            } else if(scoreValue <= 100) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_4);
            } else if(scoreValue == 100) {
                scoreImage.setImageResource(R.drawable.restaurant_score_star_5);
            }
        } else {
            LogUtil.e(TAG, "SimpleListCardItem setScoreImage score == null");
        }
    }

    private void openLink(String url) {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
        GnVoiceAssistApplication.getInstance().startActivity(mIntent);
    }

}
