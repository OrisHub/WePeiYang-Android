package com.twtstudio.tjliqy.party.ui.study.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.twtstudio.tjliqy.party.R;
import com.twtstudio.tjliqy.party.R2;
import com.twtstudio.tjliqy.party.bean.CourseDetailInfo;
import com.twtstudio.tjliqy.party.interactor.StudyInteractorImpl;
import com.twtstudio.tjliqy.party.ui.BaseActivity;
import com.twtstudio.tjliqy.party.ui.study.StudyPresenter;
import com.twtstudio.tjliqy.party.ui.study.StudyPresenterImpl;
import com.twtstudio.tjliqy.party.ui.study.answer.StudyAnswerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tjliqy on 2016/8/18.
 */
public class StudyDetailListActivity extends BaseActivity implements StudyDetailListView {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.rv_study_detail)
    RecyclerView recyclerView;
    @BindView(R2.id.bt_study_answer)
    Button btStudyAnswer;

    private Intent intent;

    private int id;

    private StudyPresenter presenter;

    private StudyDetailAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_party_study_detail_list;
    }

    @Override
    public void preInitView() {
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        adapter = new StudyDetailAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        presenter = new StudyPresenterImpl(this, new StudyInteractorImpl());
        presenter.getCourseDetail(id);
    }

    @Override
    public void afterInitView() {

    }

    @Override
    public Toolbar getToolbar() {
        toolbar.setTitle("文章列表");
        return toolbar;
    }

    @Override
    public int getMenu() {
        return 0;
    }

    public static void actionStart(Context context, int id) {
        Intent intent = new Intent(context, StudyDetailListActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public void onGetCourseDetail(List<CourseDetailInfo.DataBean> list) {
        adapter.addCourse(list);
    }

    @OnClick(R2.id.bt_study_answer)
    public void onClick() {
        StudyAnswerActivity.actionStart(this,id);
    }
}
