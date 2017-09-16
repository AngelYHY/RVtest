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

import static freestar.rvtest.R.id.irc;


public class MainActivity extends AppCompatActivity implements MultAdapter.IDialogKeyBoard, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.ntb)
    NormalTitleBar mNtb;
    @Bind(irc)
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
    private MultAdapter mAdapter;
    private int mHeight;
    private int mSelectCommentItemOffset;
    private int offset;
    //    private int mSelectCircleItemH;
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
                startActivity(new Intent(MainActivity.this, Main3Activity.class));
            }
        });

        mManager = new TopLayoutManager(this);
        mIrc.setLayoutManager(mManager);
        View view = View.inflate(this, R.layout.item_zone_header, null);

        mAdapter = new MultAdapter(ContansData.backDatas(), this);
        mAdapter.addHeaderView(view);
        mIrc.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mIrc);

        mIrc.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position + mAdapter.getHeaderLayoutCount();
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

//        setViewTreeObserver();

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                offset += mIdKeyboardVg.getHeight();
                mManager.setOffset(offset);
                Logger.e("keyBoardShow height:" + height + "-mPosition--" + mPosition + "--" + mAdapter.getItemCount() + "--" + offset);
                mIrc.smoothScrollToPosition(mPosition);
                offset = 0;
            }

            @Override
            public void keyBoardHide(int height) {
                Logger.e("keyBoardHide height:" + height + "-mPosition--" + mPosition);

//                mManager.setOffset(mIdKeyboardVg.getHeight());
//                mIrc.smoothScrollToPosition(mPosition + mAdapter.getHeaderLayoutCount());
            }
        });

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

        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType())) {
            mIdEditext.setHint("回复" + commentConfig.getName() + ":");
            mPosition = commentConfig.getCirclePosition();
            measureCircleItemHighAndCommentItemOffset(commentConfig);
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

        View selectCircleItem = mManager.findViewByPosition(mPosition);//  private LinearLayoutManager linearLayoutManager;

        if (selectCircleItem != null) {
            mHeight = selectCircleItem.getHeight();
            Logger.e("mHeight--" + mHeight);
//            mSelectCircleItemH = selectCircleItem.getHeight() - DisplayUtil.dip2px(48);//    private int mSelectCircleItemH;
            //获取评论view,计算出该view距离所属动态底部的距离
            if (commentConfig.commentType == CommentConfig.Type.REPLY) {
                Logger.e("== CommentConfig.Type.REPLY");
                //回复评论的情况
                CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
                if (commentLv != null) {
                    //找到要回复的评论view,计算出该view距离所属动态底部的距离
                    View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                    Logger.e("commentLv!= null");
                    if (selectCommentItem != null) {
                        //选择的 commentItem 距选择的 CircleItem 底部的距离
                        mSelectCommentItemOffset = 0;
                        View parentView = selectCommentItem;
                        int i = 0;
                        do {
                            // 相当于 当前 view 的底部 距离 父 view 的高度
                            int subItemBottom = parentView.getBottom();

                            parentView = (View) parentView.getParent();
                            if (parentView != null) {
                                i++;
                                // 累加 当前 view 的底部 距离 父 view 底部 的高度差
                                mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                                Logger.e(i + "--subItemBottom--" + subItemBottom + "--height--" + (parentView.getHeight() - subItemBottom));
                            }
                        }
                        while (parentView != null && parentView != selectCircleItem);

                        offset -= mSelectCommentItemOffset;
                        Logger.e("selectCommentItem != null----mHeight--" + mHeight + "-offset-" + offset + "--i--" + i);
                    }
                }
            }
        }

    }

    @Override
    public void onLoadMoreRequested() {
        mAdapter.addData(ContansData.backDatas());
        mAdapter.loadMoreComplete();
//        mAdapter.loadMoreEnd();
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
