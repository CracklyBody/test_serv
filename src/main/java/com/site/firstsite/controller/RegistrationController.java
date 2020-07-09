package com.site.firstsite.controller;

import com.site.firstsite.domain.User;
import com.site.firstsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${upload.path}")
    private String uploadPath;

    /**
     *  Добавление нового пользователя в базу данных.
     *  Все данные в формате JSON
     *
     * @param user принимает данные пользователя
     * @return ResponseEntity содержит id пользователя и статус запроса
     * */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public Callable<ResponseEntity> addUser(@RequestBody User user
                                    ) throws IOException {

        User userFromDb = userRepository.findByUsername(user.getUsername());
        return() ->{
          try{
              if(userFromDb != null){
                  return new ResponseEntity(HttpStatus.CONFLICT);
              }
              user.setStatusTimestamp(new Timestamp((new Date()).getTime()));
              loadFile(user);
              userRepository.save(user);
              return new ResponseEntity<>(user.getId(), HttpStatus.CREATED);
          } catch (Exception e){
              return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
          }
        };
    }
    /**
     *  Загрузка файла по пути указанным пользователем
     *  и добавление копии этого файла с уникальным UUID
     *  в путь указанный в настройках сервера
     *
     * @param user сам пользователь у которого загружается картинка
     * */
    private void loadFile(User user) throws IOException{
        if(user.getUrim()!=null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String file_path = user.getUrim();
            String file_name = file_path.substring(file_path.lastIndexOf("/")+1);
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "."+file_name;
            MultipartFile multipartFile = new MockMultipartFile(file_name, new FileInputStream(new File(file_path)));
            multipartFile.transferTo(new File(uploadPath + "/"+ resultFileName));
            user.setUrim(resultFileName);
        }
    }
}
