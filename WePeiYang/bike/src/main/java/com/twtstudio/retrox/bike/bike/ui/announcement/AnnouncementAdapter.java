package com.twtstudio.retrox.bike.bike.ui.announcement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twtstudio.retrox.bike.R;
import com.twtstudio.retrox.bike.R2;
import com.twtstudio.retrox.bike.common.ui.BaseAdapter;
import com.twtstudio.retrox.bike.common.ui.BaseViewHolder;
import com.twtstudio.retrox.bike.model.BikeAnnouncement;
import com.twtstudio.retrox.bike.utils.TimeStampUtils;

import butterknife.BindView;


/**
 * Created by jcy on 16-8-20.
 */
public class AnnouncementAdapter extends BaseAdapter<BikeAnnouncement> {

    private static final int ITEM_NORMAL = 0;
    private static final int ITEM_FOOTER = 1;

    static class sAnnouncementHolder extends BaseViewHolder{
        @BindView(R2.id.bike_announcement_title)
        TextView mTitleView;
        @BindView(R2.id.bike_announcement_time)
        TextView mTimeView;
        @BindView(R2.id.bike_announcement_crad)
        CardView mCardView;

        public sAnnouncementHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterHolder extends BaseViewHolder {

        @BindView(R2.id.pb_footer)
        ProgressBar mFooter;
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    public AnnouncementAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == ITEM_FOOTER){
            return new FooterHolder(inflater.inflate(R.layout.footer,parent,false));
        }else {
            return new sAnnouncementHolder(inflater.inflate(R.layout.item_bike_announcement,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ITEM_NORMAL){
            sAnnouncementHolder itemHolder = (sAnnouncementHolder) holder;
            final BikeAnnouncement item = mDataSet.get(position);
            itemHolder.mTitleView.setText(item.title);
            //itemHolder.mContentView.loadData(item.content,"text/html;charset=utf-8",null);
            itemHolder.mTimeView.setText(TimeStampUtils.getSimpleDateString(item.timestamp));
            itemHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,AnnouncementDetail.class);
                    intent.putExtra("detail",item.content);
                    intent.putExtra("title",item.title);
                    intent.putExtra("time",TimeStampUtils.getSimpleDateString(item.timestamp));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataSet.size()) {
            return ITEM_FOOTER;
        } else {
            return ITEM_NORMAL;
        }
    }
}
