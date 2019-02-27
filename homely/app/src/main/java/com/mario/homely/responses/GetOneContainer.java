package com.mario.homely.responses;

public class GetOneContainer<T> {
    private long count;
    private T rows;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
