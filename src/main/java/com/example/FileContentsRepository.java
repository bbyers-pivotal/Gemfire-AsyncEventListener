package com.example;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileContentsRepository extends CrudRepository<FileContents, String> {
    FileContents findByFilename(String filename);
    List<FileContents> findAll();
}
