package freestar.rvtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity2 extends AppCompatActivity {

    private RecyclerView irc;
    private int mViewHeight;
    private TopLayoutManager mManager;
    private int mCurrentKeyboardH;
    private int mScreenHeight;
    private boolean isShow;
    private EditText met;
    private int mPosition;
    private CommentConfig mCommentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.init("FreeStar").methodCount(1).hideThreadInfo();

        irc = (RecyclerView) findViewById(R.id.rv);
        met = ((EditText) findViewById(R.id.et));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                list.add("这是第" + i + "个 item");
            } else {
                list.add("这是第" + i + "个 item<br/>余数为：" + (i % 3));
            }
        }
        mManager = new TopLayoutManager(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        irc.setLayoutManager(mManager);

        MyAdapter adapter = new MyAdapter(list);

        irc.setAdapter(adapter);

//        irc.addOnItemTouchListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                int offset = mScreenHeight - mCurrentKeyboardH - mViewHeight - view.getHeight();   // 向上滑动是 -
//
//                Logger.e("mScreenHeight-" + mScreenHeight + "-view.getHeight()-" + view.getHeight() + "-mCurrentKeyboardH-" + mCurrentKeyboardH + "-mViewHeight-" + mViewHeight);
//
////                mManager.scrollToPositionWithOffset(position, offset);
////                mManager.scrollToPositionWithOffset(200, 200);
////                irc.scrollToPosition(99);
//
//            }
//        });

        //监听recyclerview滑动
        setViewTreeObserver();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mViewHeight = findViewById(R.id.ll).getHeight();
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 监听recyclerview滑动
     */
    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = irc.getViewTreeObserver();
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
                irc.smoothScrollToPosition(mPosition);
//                mManager.smoothScrollToPosition();
//                mManager.scrollToPosition();
//                irc.scrollToPosition(mPosition);
//                irc.scrollBy();
            }
        });
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void go(View view) {
//        if (!isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//            isShow = true;
//        }
        mPosition = Integer.parseInt(met.getText().toString());
    }

//    @Override
//    public void keyBoardOnClickListener(CommentConfig commentConfig) {
//        //执行键盘弹出
//        if (mIdKeyboardVg.getVisibility() == View.VISIBLE) {
//            updateEdittextBodyVisible(View.GONE, commentConfig);
//        } else if (mIdKeyboardVg.getVisibility() == View.GONE) {
//            updateEdittextBodyVisible(View.VISIBLE, commentConfig);
//        }
//    }
//
//    private void updateEdittextBodyVisible(int visibility, CommentConfig commentConfig) {
//        mCommentConfig = commentConfig;
//        mIdKeyboardVg.setVisibility(visibility);//LinearLayout
//        measureCircleItemHighAndCommentItemOffset(commentConfig);
//        if (commentConfig != null && CommentConfig.Type.REPLY.equals(commentConfig.getCommentType())) {
//            mIdEditext.setHint("回复" + commentConfig.getName() + ":");
//        } else {
//            mIdEditext.setHint("说点什么吧");
//        }
//        if (View.VISIBLE == visibility) {
//            //弹出键盘
//            mIdEditext.requestFocus();
//            KeyBordUtil.showSoftKeyboard(mIdEditext);
//        } else if (View.GONE == visibility) {
//            //隐藏键盘
//            KeyBordUtil.hideSoftKeyboard(mIdEditext);
//        }
//    }
//
//    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
////        if (commentConfig == null)
////            return;
////        int headViewCount = irc.getHeaderContainer().getChildCount();//IRecyclerView
////        //当前选中的view
////        int selectPostion = commentConfig.circlePosition + headViewCount + 1;//public int circlePosition;
////        View selectCircleItem = linearLayoutManager.findViewByPosition(selectPostion);//  private LinearLayoutManager linearLayoutManager;
////
////        if (selectCircleItem != null) {
////            mHeight = selectCircleItem.getHeight();
////            mSelectCircleItemH = selectCircleItem.getHeight() - DisplayUtil.dip2px(48);//    private int mSelectCircleItemH;
////            //获取评论view,计算出该view距离所属动态底部的距离
////            if (commentConfig.commentType == CommentConfig.Type.REPLY) {
////                //回复评论的情况
////                CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
////                if (commentLv != null) {
////                    //找到要回复的评论view,计算出该view距离所属动态底部的距离
////                    View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
////                    if (selectCommentItem != null) {
////                        //选择的commentItem距选择的CircleItem底部的距离
////                        mSelectCommentItemOffset = 0;
////                        View parentView = selectCommentItem;
////                        do {
////                            int subItemBottom = parentView.getBottom();
////                            parentView = (View) parentView.getParent();
////                            if (parentView != null) {
////                                mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
////                            }
////                        }
////                        while (parentView != null && parentView != selectCircleItem);
////                    }
////                }
////            }
////        }
//
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//
//    }
}
