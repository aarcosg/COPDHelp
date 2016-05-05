package com.aarcosg.copdhelp.mvp.view.medicalattention;

import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.ui.adapter.MedicalAttentionItem;

import java.util.List;

public interface MedicalAttentionMainView extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindMedicalAttentions(List<MedicalAttentionItem> medicalAttentions);
}
