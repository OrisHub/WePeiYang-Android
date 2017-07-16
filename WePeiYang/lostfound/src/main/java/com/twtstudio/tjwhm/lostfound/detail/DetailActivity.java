package com.twtstudio.tjwhm.lostfound.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twtstudio.tjwhm.lostfound.R;
import com.twtstudio.tjwhm.lostfound.base.BaseActivity;
import com.twtstudio.tjwhm.lostfound.success.SuccessActivity;
import com.twtstudio.tjwhm.lostfound.support.Utils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tjwhm & liuyuesen on 2017/7/5.
 **/

public class DetailActivity extends BaseActivity implements DetailContract.DetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_pic)
    ImageView detail_pic;
    @BindView(R.id.detail_title)
    TextView detail_title;
    @BindView(R.id.detail_time)
    TextView detail_time;
    @BindView(R.id.detail_place)
    TextView detail_place;
    @BindView(R.id.detail_type)
    TextView detail_type;
    @BindView(R.id.detail_name)
    TextView detail_name;
    @BindView(R.id.detail_phone)
    TextView detail_phone;
    @BindView(R.id.detail_rematks)
    TextView detail_remarks;
    @BindView(R.id.detail_layout_without_pic)
    LinearLayout detail_layout_without_pic;
    AlertDialog.Builder builder;
    DetailBean detailData;

    DetailContract.DetailPresenter detailPresenter = new DetailPresenterImpl(this);

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail;
    }

    @Override
    protected Toolbar getToolbarView() {
        toolbar.setTitle("物品详情");
        return toolbar;
    }

    @Override
    protected int getToolbarMenu() {
        return R.menu.detail_menu;
    }

    @Override
    protected void setToolbarMenuClickEvent() {
        super.setToolbarMenuClickEvent();
        toolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.detail_share) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("index", "share");
                intent.putExtras(bundle);
                intent.setClass(this, SuccessActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }

    @Override
    protected boolean isShowBackArrow() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        detailPresenter.loadDetailData(id);

    }

    @Override
    public void setDetailData(DetailBean detailData) {
        this.detailData = detailData;

        detail_title.setText(detailData.data.title);
        detail_time.setText(detailData.data.time);
        detail_place.setText(detailData.data.place);
        detail_type.setText(Utils.getType(detailData.data.detail_type));
        detail_name.setText(detailData.data.name);
        detail_phone.setText(detailData.data.phone);
        detail_remarks.setText(detailData.data.item_description);

        if (Objects.equals(detailData.data.picture, "") || detailData.data.picture == null) {
            Glide.with(this)
                    .load(Utils.noPicForDetail())
                    .asBitmap()
                    .override(getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth(),
                            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight()
                                    - detail_layout_without_pic.getHeight()
                                    - toolbar.getHeight())
                    .fitCenter()
                    .into(detail_pic);
        } else {
            Glide.with(this)
                    .load(Utils.getPicUrl(detailData.data.picture))
                    .asBitmap()
                    .override(getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth(),
                            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight()
                                    - detail_layout_without_pic.getHeight()
                                    - toolbar.getHeight())
                    .fitCenter()
                    .into(detail_pic);
        }

        detail_phone.setOnClickListener(view -> {
            Uri uri = Uri.parse("tel:" + detailData.data.phone);

            Intent intent = new Intent(Intent.ACTION_DIAL, uri);

            startActivity(intent);
        });
        if (detailData.data.picture != null && !Objects.equals(detailData.data.picture, "")) {
            detail_pic.setOnClickListener(this::showPicDialog);
        }
    }

    private void showPicDialog(View view) {
        builder = new AlertDialog.Builder(this);

        LinearLayout picDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_detail_pic, null);

        builder.setView(picDialog);
        AlertDialog dialog = builder.create();
        ImageView detail_dialog_pic = ButterKnife.findById(picDialog, R.id.detail_bigpic);

        if (detail_dialog_pic != null) {
            Glide.with(this)
                    .load(Utils.getPicUrl(detailData.data.picture))
                    .into(detail_dialog_pic);

        }
        dialog.show();
    }
}
