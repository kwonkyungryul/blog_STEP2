package shop.mtcoding.blog.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.handler.ex.CustomApiException;

public class PathUtil {

    private static String getStaticFolder(){
        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\";
    }

    public static String writeImageFile(MultipartFile profile){
        UUID uuid = UUID.randomUUID(); // 파일명이 중복되지 않게 하기 위한 랜덤한 문자 생성
        String uuidImageRealName = "images\\"+uuid+"_"+profile.getOriginalFilename();
        
        String staticFolder = getStaticFolder();
        Path imageFilePath = Paths.get(staticFolder + uuidImageRealName);
        try {
            Files.write(imageFilePath, profile.getBytes());
        }catch (Exception e){
            throw new CustomApiException("사진을 웹서버에 저장하지 못하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "/images/"+uuid+"_"+profile.getOriginalFilename();
    }
}