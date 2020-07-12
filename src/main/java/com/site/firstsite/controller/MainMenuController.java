package com.site.firstsite.controller;

import com.site.firstsite.domain.User;
import com.site.firstsite.exceptions.NotFoundException;
import com.site.firstsite.repository.UserRepository;
import com.site.firstsite.response.ResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/main")
public class MainMenuController {
    @Autowired
    UserRepository userRepository;

    /**
     *  Берем список пользователей и
     *  возвращаем список хэш-таблиц
     *  где будут только имя, статус и urim
     *
     * @param users список пользователей для обработки
     * @return список хэш-таблиц
     * */
    private List<Map<String,String>> getInformation(List<User> users){
        List<Map<String,String>> result= new ArrayList<Map<String,String>>();
        for (int i=0;i<users.size();i++) {
            Map<String,String> res= new HashMap<String,String>();
            res.put("username",users.get(i).getUsername());
            res.put("status",users.get(i).isStatus() ? "true" : "false");
            res.put("urim",users.get(i).getUrim());
            result.add(res);
        }
        return result;
    }

    /**
     *  Обновление информации о пользователе.
     *  Данные на выходе в формате JSON.
     *
     * @param id Уникальный идентификатор пользователя
     * @param status Новый статус пользователя true/false
     * @return ResponseEntity содержит http статус и объект ResponseJson
     * id идентификатор пользователя
     * statusBefore статус пользователя до вызова функции
     * statusAfter статус пользователя после вызова функции
     * */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Order
    public Callable<ResponseEntity> update(@PathVariable String id,
                                           @RequestBody String status) throws NotFoundException {
        return () ->{
            Timestamp updateTime = new Timestamp((new Date()).getTime());
            User usersFromDb = userRepository.findById(Long.parseLong(id));
            try{
                if(usersFromDb==null){
                    throw new NotFoundException("NotFound");
                }
                boolean stat_bef= usersFromDb.isStatus();
                usersFromDb.setStatus(Boolean.getBoolean(status));
                userRepository.saveAndFlush(usersFromDb);
                ResponseJson res = new ResponseJson();
                res.setId(new Long(id));
                res.setStatusAfter(Boolean.getBoolean(status));
                res.setStatusBefore(stat_bef);
                return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
            }
            catch (Exception e){
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }
    /**
     *  Фильтрация пользователей по статусу online/offline и id timestamp.
     *  Параметры запроса могут отсутсвовать.
     *
     * @param status фильтр пользователей по статутсу online/offline
     * @param id идентификатор запроса, используется для определения пользователей,
     *          сменивших статус за прошедшее время
     * @return Список хэш-таблиц информаций пользователей
     **/
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public List<Map<String,String>> filter(@RequestParam(value = "status", required = false) Boolean status,
                                           @RequestParam(value = "id", required = false) Long id){
        if(id!=null){
            Timestamp timestamp = new Timestamp(id);
            if(status!=null){
                return getInformation(userRepository.findByStatusAndStatusTimestampAfter(status,timestamp));
            }
            return getInformation(userRepository.findByStatusTimestampAfter(timestamp));
        }
        else
            if(status!=null){
                return getInformation(userRepository.findByStatus(status));
            }
            return getInformation(userRepository.findAll());
    }
}
