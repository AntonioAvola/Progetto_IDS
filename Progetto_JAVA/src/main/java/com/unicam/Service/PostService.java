package com.unicam.Service;

import com.unicam.Model.PostTurista;
import com.unicam.Repository.IContenutoRepository;
import com.unicam.Repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PostService {

    @Autowired
    private IPostRepository postTuristaRepository;


}
