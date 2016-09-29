package com.example;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.Region;

import java.io.Serializable;

@Region("syncfiles")
public class FileContents implements Serializable {

    @Id
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @PersistenceConstructor
    public FileContents(String filename, byte[] contents) {
        this.filename = filename;
        this.contents = contents;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    private byte[] contents;

    @Override
    public String toString() {
        return filename;
    }
}
