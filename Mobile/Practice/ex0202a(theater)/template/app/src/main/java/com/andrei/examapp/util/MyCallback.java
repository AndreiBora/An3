package com.andrei.examapp.util;

import com.andrei.examapp.model.GenericEntity;

public interface MyCallback {
    void onConfirm(GenericEntity entity);
    void onReserved(GenericEntity entity);
}
