package com.senerunosoft.ironbuff.table;

import java.util.Date;
import java.util.List;

public class ImageTable {

    List<String> ImageUrl;
    List<Date> dates;

    public ImageTable(List<String> imageUrl, List<Date> dates) {
        ImageUrl = imageUrl;
        this.dates = dates;
    }

    public ImageTable() {
    }

    public List<String> getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        ImageUrl = imageUrl;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }
}
