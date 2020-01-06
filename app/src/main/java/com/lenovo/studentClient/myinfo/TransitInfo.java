package com.lenovo.studentClient.myinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**Parcelable
 * @author asus
 */
public class TransitInfo  implements Parcelable {
    private String TransitName;
    private String TransitSite;
    public static final Creator<TransitInfo> CREATOR = new Creator<TransitInfo>() {
        @Override
        public TransitInfo createFromParcel(Parcel in) {
            TransitInfo transitInfo =  new TransitInfo();
            transitInfo.TransitName = in.readString();
            transitInfo.TransitSite = in.readString();
            return transitInfo;
        }

        @Override
        public TransitInfo[] newArray(int size) {
            return new TransitInfo[size];
        }
    };

    public String getTransitName() {
        return TransitName;
    }

    public void setTransitName(String transitName) {
        TransitName = transitName;
    }

    public String getTransitSite() {
        return TransitSite;
    }

    public void setTransitSite(String transitSite) {
        TransitSite = transitSite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TransitName);
        dest.writeString(TransitSite);
    }
}
