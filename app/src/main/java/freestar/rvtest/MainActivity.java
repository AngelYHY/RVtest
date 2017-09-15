package freestar.rvtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jaydenxiao.common.commonutils.KeyBordUtil;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MultAdapter.IDialogKeyBoard, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(R.id.irc)
    RecyclerView mIrc;
    @Bind(R.id.id_editext)
    EditText mIdEditext;
    @Bind(R.id.id_fasong)
    ImageView mIdFasong;
    @Bind(R.id.id_keyboard_vg)
    LinearLayout mIdKeyboardVg;
    private boolean flag;

    //    private RecyclerView irc;
//    private int mViewHeight;
    private TopLayoutManager mManager;
    //    private int mCurrentKeyboardH;
//    private int mScreenHeight;
//    private boolean isShow;
//    private EditText met;
    private int mPosition;
    //    private CommentConfig mCommentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Logger.init("FreeStar").methodCount(1).hideThreadInfo();

        mNtb.setTitleText("动态列表");
        mNtb.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });

        mManager = new TopLayoutManager(this);
        mIrc.setLayoutManager(mManager);
        View view = View.inflate(this, R.layout.item_zone_header, null);

        MultAdapter adapter = new MultAdapter(ContansData.backDatas(), this);
//        adapter.addHeaderView(view);
        mIrc.setAdapter(adapter);
//        adapter.setOnLoadMoreListener(this, mIrc);

        mIrc.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position;
                updateEdittextBodyVisible(View.VISIBLE, null);
            }
        });

        mIrc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mIdKeyboardVg.getVisibility() == View.VISIBLE)
                    updateEdittextBodyVisible(View.GONE, null);
                return false;
            }
        });

        setViewTreeObserver();

    }

    /**
     * 监听recyclerview滑动
     */
    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mIrc.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int height = mIdKeyboardVg.getHeight();

                mManager.setOffset(height);
                if (flag) {
                    Logger.e("mPosition--" + mPosition + "-mViewHeight-" + height);
                    mIrc.smoothScrollToPosition(mPosition);
                    flag = false;
                }

            }
        });
    }

    @Override
    public void keyBoardOnClickListener(CommentConfig commentConfig) {
        //执行键盘弹出
//        if (mIdKeyboardVg.getVisibility() == View.VISIBLE) {
//            updateEdittextBodyVisible(View.GONE, commentConfig);
//        } else if (mIdKeyboardVg.getVisibility() == View.GONE) {
        updateEdittextBodyVisible(View.VISIBLE, commentConfig);
//        }
    }

    private void updateEdittextBodyVisible(int visibility, CommentConfig commentConfig) {
//        Logger.e("visibility--" + visibility);
        mIdKeyboardVg.setVisibility(visibility);//LinearLayout
        flag = true;
//        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType())) {
            mPosition = commentConfig.getCirclePosition();
            mIdEditext.setHint("回复" + commentConfig.getName() + ":");
        } else {
            mIdEditext.setHint("说点什么吧");
        }
        if (View.VISIBLE == visibility) {
            //弹出键盘
            mIdEditext.requestFocus();
            KeyBordUtil.showSoftKeyboard(mIdEditext);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            KeyBordUtil.hideSoftKeyboard(mIdEditext);
//            mIdKeyboardVg.setVisibility(View.GONE);
        }

//        int height = mIdKeyboardVg.getHeight();
//        Logger.e("mPosition--" + mPosition + "-mViewHeight-" + height);
//
//        mManager.setOffset(height);
//        mIrc.smoothScrollToPosition(mPosition);
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
//        if (commentConfig == null)
//            return;
//        int headViewCount = irc.getHeaderContainer().getChildCount();//IRecyclerView
//        //当前选中的view
//        int selectPostion = commentConfig.circlePosition + headViewCount + 1;//public int circlePosition;
//        View selectCircleItem = linearLayoutManager.findViewByPosition(selectPostion);//  private LinearLayoutManager linearLayoutManager;
//
//        if (selectCircleItem != null) {
//            mHeight = selectCircleItem.getHeight();
//            mSelectCircleItemH = selectCircleItem.getHeight() - DisplayUtil.dip2px(48);//    private int mSelectCircleItemH;
//            //获取评论view,计算出该view距离所属动态底部的距离
//            if (commentConfig.commentType == CommentConfig.Type.REPLY) {
//                //回复评论的情况
//                CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
//                if (commentLv != null) {
//                    //找到要回复的评论view,计算出该view距离所属动态底部的距离
//                    View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
//                    if (selectCommentItem != null) {
//                        //选择的commentItem距选择的CircleItem底部的距离
//                        mSelectCommentItemOffset = 0;
//                        View parentView = selectCommentItem;
//                        do {
//                            int subItemBottom = parentView.getBottom();
//                            parentView = (View) parentView.getParent();
//                            if (parentView != null) {
//                                mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
//                            }
//                        }
//                        while (parentView != null && parentView != selectCircleItem);
//                    }
//                }
//            }
//        }

    }

    @Override
    public void onLoadMoreRequested() {

    }

//    protected void onResume() {
//        super.onResume();
//        //获取当前屏幕内容的高度
//        screenHeight = getWindow().getDecorView().getHeight();
//        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                //获取View可见区域的bottom
//                Rect rect = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                if(bottom!=0 && oldBottom!=0 && bottom - rect.bottom <= 0){
//                    Toast.makeText(Main3Activity.this, "隐藏", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(Main3Activity.this, "弹出", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
