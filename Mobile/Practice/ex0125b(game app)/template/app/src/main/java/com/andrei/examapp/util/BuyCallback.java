package com.andrei.examapp.util;

import com.andrei.examapp.model.GenericEntity;

import java.util.List;

public interface BuyCallback {

    void onBuy(List<GenericEntity> list);
}
