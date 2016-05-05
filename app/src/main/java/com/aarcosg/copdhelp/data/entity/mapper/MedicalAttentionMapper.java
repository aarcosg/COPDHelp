package com.aarcosg.copdhelp.data.entity.mapper;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;
import com.aarcosg.copdhelp.ui.adapter.MedicalAttentionItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MedicalAttentionMapper {

    public static MedicalAttentionItem transform(MedicalAttention medicalAttention) {
        MedicalAttentionItem item = null;
        if (medicalAttention != null) {
            item = new MedicalAttentionItem(
                    medicalAttention.getId()
                    ,medicalAttention.getType()
                    ,medicalAttention.getTime()
                    ,medicalAttention.getNote());
        }
        return item;
    }

    public static List<MedicalAttentionItem> transform(Collection<MedicalAttention> medicalAttentionCollection) {
        List<MedicalAttentionItem> itemList = new ArrayList<>(medicalAttentionCollection.size());
        MedicalAttentionItem item;
        for (MedicalAttention medicalAttention : medicalAttentionCollection) {
            item = transform(medicalAttention);
            if (item != null) {
                itemList.add(item);
            }
        }
        return itemList;
    }
}
