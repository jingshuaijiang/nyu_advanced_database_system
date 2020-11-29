package com.company;

public class Variable {
    int value;
    int version;

    public Variable(int version, int value)
    {
        this.version = version;
        this.value = value;
    }

    public int getValue()   {   return this.value;      }
    public int getVersion() {   return this.version;    }
}
