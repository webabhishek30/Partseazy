package com.partseazy.android.ui.model.deal.demo;

import com.partseazy.android.ui.model.deal.FileData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 22/8/17.
 */

public class DemoPostData {

    public String name;
    public String mobile;
    public String address;
    public int tradeId;
    public List<FileData> imageFileList;
    public FileData imageFile;
    public File file;

    public DemoPostData()
    {
        imageFileList = new ArrayList<>();
    }
}
