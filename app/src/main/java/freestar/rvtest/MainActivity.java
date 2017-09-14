package freestar.rvtest;

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

import static freestar.rvtest.R.id.irc;
import static freestar.rvtest.R.id.ntb;


public class MainActivity extends AppCompatActivity implements MultAdapter.IDialogKeyBoard, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(ntb)
    NormalTitleBar mNtb;
    @Bind(irc)
    RecyclerView mIrc;
    @Bind(R.id.id_editext)
    EditText mIdEditext;
    @Bind(R.id.id_fasong)
    ImageView mIdFasong;
    @Bind(R.id.id_keyboard_vg)
    LinearLayout mIdKeyboardVg;

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

        mManager = new TopLayoutManager(this);
        mIrc.setLayoutManager(mManager);
        View view = View.inflate(this, R.layout.item_zone_header, null);

        MultAdapter adapter = new MultAdapter(ContansData.backDatas(), this);
        adapter.addHeaderView(view);
        mIrc.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, mIrc);

        mIrc.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position + 1;
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
//                Rect r = new Rect();
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);//应用程序 App 的区域 包括标题栏，但不包括状态栏
//                int statusBarH = getStatusBarHeight();//状态栏高度
//                int screenH = irc.getRootView().getHeight();//在这个 demo 中是整个屏幕的大小
//                if (r.top != statusBarH) {// 判断是否为 沉浸式状态栏
//                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
//                    r.top = statusBarH;   // 这样设置时  当为沉浸式状态栏时 除去状态栏高度
//                }
//                int keyboardH = screenH - (r.bottom - r.top);// 键盘没弹出来时 keyboardH= r.top = statusBarH
//                Logger.e(mPosition + "--screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH + " &mCurrentKeyboardH=" + mCurrentKeyboardH);
//                if (keyboardH < 100) {//有变化时才处理，否则会陷入死循环
//                    return;
//                }
//
//                mCurrentKeyboardH = keyboardH;
//                mScreenHeight = screenH;//应用屏幕的高度
//                mEditTextBodyHeight = editTextBodyLl.getHeight();
//
//                //偏移listview
//                if (irc != null && mCommentConfig != null) {
//                    int index = mCommentConfig.circlePosition + irc.getHeaderContainer().getChildCount() + 1;
//                    Logger.e("circlePosition:" + mCommentConfig.circlePosition + "-index-" + index);
//                    mManager.scrollToPositionWithOffset(index, getListviewOffset(mCommentConfig));
//                }
                int height = mIdKeyboardVg.getHeight();
                Logger.e("mPosition--" + mPosition + "-mViewHeight-" + height);

                mManager.setOffset(height);
                mIrc.smoothScrollToPosition(mPosition);
//                mManager.smoothScrollToPosition();
//                mManager.scrollToPosition();
//                irc.scrollToPosition(mPosition);
//                irc.scrollBy();
            }
        });
    }

    @Override
    public void keyBoardOnClickListener(CommentConfig commentConfig) {
        //执行键盘弹出
        if (mIdKeyboardVg.getVisibility() == View.VISIBLE) {
            updateEdittextBodyVisible(View.GONE, commentConfig);
        } else if (mIdKeyboardVg.getVisibility() == View.GONE) {
            updateEdittextBodyVisible(View.VISIBLE, commentConfig);
        }
    }

    private void updateEdittextBodyVisible(int visibility, CommentConfig commentConfig) {
        Logger.e("visibility--" + visibility);
        mIdKeyboardVg.setVisibility(visibility);//LinearLayout
        measureCircleItemHighAndCommentItemOffset(commentConfig);
        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType())) {
            mIdEditext.setHint("回复" + commentConfig.getName() + ":");
            mPosition = commentConfig.getCirclePosition();
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
        }

        int height = mIdKeyboardVg.getHeight();
        Logger.e("mPosition--" + mPosition + "-mViewHeight-" + height);

        mManager.setOffset(height);
        mIrc.smoothScrollToPosition(mPosition);
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
}
