package com.mario.homely.responses;

import java.util.List;

public class ResponseContainer<T> {
    private long count;
    private List<T> rows;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }


}
