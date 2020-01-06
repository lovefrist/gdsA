package com.lenovo.studentClient.myinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *旅游地铁信息类
 * @author asus
 */
public class AssistantInfo implements Parcelable {
    int ticket ;
    String name;
    String imgUrl;



    public static final Creator<AssistantInfo> CREATOR = new Creator<AssistantInfo>() {
        @Override
        public AssistantInfo createFromParcel(Parcel in) {
            AssistantInfo info =  new AssistantInfo();
            info.ticket = in.readInt();
            info.name = in.readString();
            info.imgUrl = in.readString();
            return info ;
        }

        @Override
        public AssistantInfo[] newArray(int size) {
            return new AssistantInfo[size];
        }
    };

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ticket);
        dest.writeString(name);
        dest.writeString(imgUrl);
    }
}
