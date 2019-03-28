package com.andrei.examapp.util;

import com.andrei.examapp.model.GenericEntity;

public interface LoanCallback {
    void onLoan(GenericEntity entity);
}
