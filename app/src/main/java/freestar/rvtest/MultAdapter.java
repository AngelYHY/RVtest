package freestar.rvtest;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/9/14
 * github：
 */

public class MultAdapter extends BaseQuickAdapter<CircleItem, BaseViewHolder> {

    private IDialogKeyBoard mIDialogKeyBoard;

    public MultAdapter(@Nullable List<CircleItem> data, IDialogKeyBoard board) {
        super(R.layout.item_circle_list, data);
        mIDialogKeyBoard = board;
    }

    public interface IDialogKeyBoard {
        void keyBoardOnClickListener(CommentConfig commentconfig);
    }

    @Override
    protected void convert(final BaseViewHolder holder, CircleItem item) {
        final List<CommentItem> commentsDatas = item.getReplys();

        boolean hasFavort = item.getGoodjobs().size() > 0;
        boolean hasComment = item.getReplys().size() > 0;

        holder.setImageResource(R.id.headIv, R.mipmap.ic_launcher)
                .setText(R.id.nameTv, item.getNickName())
                .setText(R.id.contentTv, item.getContent())
                .addOnClickListener(R.id.ll_comment);


//        ((TV) holder.getView(R.id.contentTv)).setText(item.getContent());

        CommentListView commentList = holder.getView(R.id.commentList);

//        commentList 需要动态设置显示 隐藏 否则会有复用问题

//        Logger.e("hasFavort--" + hasFavort + "-hasComment-" + hasComment + "-position-" + holder.getLayoutPosition());

        if (hasFavort || hasComment) {
            //处理评论列表
            if (hasComment) {
                commentList.setVisibility(View.VISIBLE);
                CommentAdapter adapter = new CommentAdapter(mContext);
                commentList.setAdapter(adapter);
                adapter.setDatas(commentsDatas);
                adapter.notifyDataSetChanged();
                Log.e("FreeStar", "MultAdapter→→→convert:" + commentsDatas.size());

                //点击评论
                commentList.setOnItemClick(new CommentListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {
//                        CommentItem commentItem = commentsDatas.get(commentPosition);
//                        if (AppCache.getInstance().getUserId().equals(commentItem.getUserId())) {//复制或者删除自己的评论
//                            CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position, commentPosition);
//                            dialog.show();
//                        } else {//回复别人的评论
                        if (mIDialogKeyBoard != null) {
                            CommentConfig config = new CommentConfig();
                            config.circlePosition = holder.getAdapterPosition();
                            config.commentPosition = commentPosition;
                            config.commentType = CommentConfig.Type.REPLY;
//                                config.setPublishId(circleItem.getId());
//                                config.setPublishUserId(circleItem.getUserId());//动态人userid
//                                config.setId(commentItem.getUserId());//评论人userid
//                                config.setName(commentItem.getUserNickname());//评论人nickname
                            mIDialogKeyBoard.keyBoardOnClickListener(config);
                        }
//                        }
                    }
                });
            } else {
                commentList.setVisibility(View.GONE);
            }

        } else {
            commentList.setVisibility(View.GONE);
        }

    }
}
