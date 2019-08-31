package com.khoa.retrofit_api_example.Event;

import com.khoa.retrofit_api_example.Model.AvatarImage;

public class EventLoadUrlAvatarSuccess {
    private AvatarImage avatarImage;

    public EventLoadUrlAvatarSuccess(AvatarImage avatarImage) {
        this.avatarImage = avatarImage;
    }

    public AvatarImage getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(AvatarImage avatarImage) {
        this.avatarImage = avatarImage;
    }
}
