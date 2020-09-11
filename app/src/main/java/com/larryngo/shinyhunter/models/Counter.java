package com.larryngo.shinyhunter.models;

public class Counter {
    private int id;
    private String name;
    private byte[] image;
    private byte[] platform;
    private int count;

    Counter(int id, String name, int count)
    {

    }

    Counter(int id, String name, byte[] image, byte[] platform, int count)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.platform = platform;
        this.count = count;
    }

    public void add()
    {
        count++;
    }

    public void add(int num)
    {
        count += num;
    }
}
