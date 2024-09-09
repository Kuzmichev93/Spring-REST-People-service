package app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class ImageController {
    private String path = "D:/springapp/src/main/resources/img/%s/%s";


    @GetMapping(value="img/{catalog}/{name}")
    public ResponseEntity<byte[]> getIMG(@PathVariable String name,@PathVariable String catalog) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        File file = new File(String.format(path,catalog,name));
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(bytes,0,bytes.length);

        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
    }
}
