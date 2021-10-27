package com.swipecrafts.school.utils.renderer;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public interface ClickListener<M extends BaseModel> {
    void onItemClicked(M model);
}
