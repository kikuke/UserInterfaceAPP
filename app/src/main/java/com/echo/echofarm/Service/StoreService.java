package com.echo.echofarm.Service;

import android.net.Uri;

import java.util.List;

public interface StoreService {

    void storeImage(Uri imageUri, String forderName, String photoName);

    void getAllImageUrl(String forderName, List<Uri> uriList);

    void getImageUrl(String forderName, String photoName, List<Uri> uriList);
}
