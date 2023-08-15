package com.rukshesh.executor.api.service;

import com.rukshesh.executor.api.entity.User;
import com.rukshesh.executor.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        logger.info("saving list of users of size {}", users.size(),"" + Thread.currentThread().getName());
        users = repository.saveAll(users);
        long endTime = System.currentTimeMillis();
        logger.info("Total time {}", (endTime - startTime));
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        logger.info("Get list of users by "+ Thread.currentThread().getName());
        List<User> users = repository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception{
        final List<User> users = new ArrayList<>();

        try{
            try(final BufferedReader br = new BufferedReader(new InputStreamReader((file.getInputStream())))){
                String line;
                while((line = br.readLine())!= null){
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setFirstName(data[1]);
                    user.setLastName(data[2]);
                    user.setEmail(data[3]);
                    user.setGender(data[4]);
                    user.setIpAddress(data[5]);
                    users.add(user);
                }
            }
            return users;


        }catch(final IOException ioException){
            logger.error("Failed to parse CSV file {}", ioException);
            throw new Exception("Failed to parse CSV file {}", ioException);
        }

    }
}
