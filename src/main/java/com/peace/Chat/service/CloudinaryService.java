package com.peace.Chat.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String upLoadFile(MultipartFile file, String folder)throws IOException{
        Map uploadResult= cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                 "folder", folder,
                        "resource_type", "auto"
                )
        );
        return uploadResult.get("secure_url").toString();
    }
}
