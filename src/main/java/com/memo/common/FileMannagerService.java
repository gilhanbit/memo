package com.memo.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component // Spring bean
public class FileManagerService {
    // 실제 업로드 된 이미지 파일이 저장될 경로
    public final static String FILE_UPLOAD_PATH = "D:\\신보람\\6_spring_project\\memo_image/";

    // i: multipartFile, userLoginId(폴더명으로 사용)
    // o: imagePath(String)
    public String uploadFile(MultipartFile file, String userLoginId) {
        // 폴더(directory) 생성
        // 예: aaaa_1728349208/sun.png
        String directoryName = userLoginId + "_" + System.currentTimeMillis();
        String filePath = FILE_UPLOAD_PATH + directoryName + "/"; // D:\신보람\6_spring_project\memo_image/memo_image/aaaa_1728349208/

        // 디렉토리 생성
        File directory = new File(filePath);
        if (directory.mkdir() == false) {
            return null; // 폴더 생성시 실패하면 이미지 경로를 null. (에러 아님)
        }

        // 파일 업로드
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath + file.getOriginalFilename()); // // D:\신보람\6_spring_project\memo_image/memo_image/aaaa_1728349208/sun.png
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace(); // 에러 이유 출력
            return null; // 이미지 업로드 실패시 경로는 null (에러 아님)
        }

        // 파일업로드가 성공하면 imagePath 리턴
        // 이미지 경로는 이렇게 될 것이다(예언)
        // http://localhost/images/폴더명/이미지이름
        // /images/aaaa_1728349208/sun.png
        return "/images/" + directoryName + "/" + file.getOriginalFilename();
    }
}