package fr.nicolashoareau_toulousewcs.appliwcsprojet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import fr.nicolashoareau_toulousewcs.appliwcsprojet.R;
import fr.nicolashoareau_toulousewcs.appliwcsprojet.model.ChatModel;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

    private ArrayList<ChatModel> mChatModels;
    private String mName;
    private Context mContext;

    public static final int SENDER = 0;
    public static final int RECEIVER = 1;

    public ChatRecyclerAdapter(Context context, ArrayList<ChatModel> chatModels, String name) {
        mChatModels = chatModels;
        mName = name;
        mContext = context;
    }

    @Override
    public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_received, parent, false);
            ViewHolder viewHolder = new ViewHolder((LinearLayout) itemView);
            return  viewHolder;
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_send, parent, false);
            ViewHolder viewHolder = new ViewHolder((LinearLayout) itemView);
            return  viewHolder;
        }
    }


    @Override
    public void onBindViewHolder(ChatRecyclerAdapter.ViewHolder holder, int position) {
        final ChatModel chatModel = mChatModels.get(position);
        holder.tvName.setText(chatModel.getName());
        holder.tvMessage.setText(chatModel.getMsg());

    }

    @Override
    public int getItemCount() {
        return mChatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = mChatModels.get(position);
        if (chatModel.getName().equals(mName)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMessage;
        public ViewHolder(View v) {
            super(v);
            this.tvName = v.findViewById(R.id.tv_name);
            this.tvMessage = v.findViewById(R.id.tv_msg);
        }
    }
}
