package org.ar.ar_rtm_tutorial_android;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class MessageApdater extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public MessageApdater() {
        super(R.layout.item_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {
        if (item.isSelf){
            helper.setVisible(R.id.card_other,false);
            helper.setVisible(R.id.card_self,true);
            helper.setText(R.id.tv_self,item.name.isEmpty() ? item.content : item.name+":"+item.content);
        }else {
            helper.setVisible(R.id.card_other,true);
            helper.setVisible(R.id.card_self,false);
            helper.setText(R.id.tv_other,item.name.isEmpty() ? item.content : item.name+":"+item.content);
        }


    }
}
