package freestar.rvtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Main3Activity extends AppCompatActivity {

    @Bind(R.id.commentList)
    CommentListView mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);

        List<CommentItem> replay5 = new ArrayList<>();
        replay5.add(new CommentItem("1006", "曹操", "1008", "关羽", "善sf !"));
        replay5.add(new CommentItem("1007", "曹操", "1009", "关羽", "善sa!"));
        replay5.add(new CommentItem("1008", "曹操", "1010", "关羽", "善fd!"));
        replay5.add(new CommentItem("1009", "曹操", "1050", "关羽", "善ga!"));

        CommentAdapter adapter = new CommentAdapter(this);
        mCommentList.setAdapter(adapter);
        adapter.setDatas(replay5);
        adapter.notifyDataSetChanged();

        final View view = findViewById(R.id.ll);

        mCommentList.setOnItemClick(new CommentListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                View selectCommentItem = mCommentList.getChildAt(position);
                if (selectCommentItem != null) {
                    //选择的 commentItem 距选择的 CircleItem 底部的距离
                    int mSelectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    int offset = 0;
                    int i = 0;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView == mCommentList) {
                            Logger.e("i--" + i);
                        }
                        if (parentView != null) {
                            i++;
                            mSelectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                            Logger.e(i + "--subItemBottom--" + subItemBottom + "--height--" + (parentView.getHeight() - subItemBottom));
                        }
                    }
                    while (parentView != null && parentView != view);

                    offset -= mSelectCommentItemOffset;
                    Logger.e("-offset-" + offset + "--i--" + i);
                }
            }
        });

    }
}
