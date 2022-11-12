package com.echo.echofarm.Activity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UploadedPhotoData implements Parcelable {
    private Uri photoUri;

    public UploadedPhotoData(Uri uri) { photoUri = uri; }

    public Uri getPhotoUri() {
        return photoUri;
    }
    public void setPhotoUri(Uri newUri) {
        photoUri = newUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photoUri.toString());
    }

    public static final Creator<UploadedPhotoData> CREATOR = new Creator<UploadedPhotoData>() {
        @Override
        public UploadedPhotoData createFromParcel(Parcel parcel) {
            String sUri = parcel.readString();
            return new UploadedPhotoData(Uri.parse(sUri));
        }

        @Override
        public UploadedPhotoData[] newArray(int i) {
            return new UploadedPhotoData[i];
        }
    };
}
