package com.example;


import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileProcessorController {

    FileContentsRepository repository;

    FileProcessorController(FileContentsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/{filename}", method = RequestMethod.POST)
    public ResponseEntity sendFile(@PathVariable String filename) {
        InputStream is = getClass().getResourceAsStream("/bacon.txt");
        byte[] bacon = null;

        try {
            bacon = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileContents fc = new FileContents(filename, bacon);
        repository.save(fc);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    public String getFilenames(@PathVariable String filename) {
        FileContents file = repository.findByFilename(filename);
        return new String(file.getContents());
    }

}
