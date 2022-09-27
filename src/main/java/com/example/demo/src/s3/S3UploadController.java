package com.example.demo.src.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
//@RequestMapping("")
public class S3UploadController {
    private String S3Bucket = "roger-bucket"; // Bucket 이름

    @Autowired
    AmazonS3Client amazonS3Client;

    //    @GetMapping("/upload")
//    public ResponseEntity<Object> upload(MultipartFile[] multipartFileList) throws Exception {
    public List<String> upload(MultipartFile[] multipartFileList) throws Exception {
        List<String> imagePathList = new ArrayList<>();

        for(MultipartFile multipartFile: multipartFileList) {
            String originalName = multipartFile.getOriginalFilename(); // 파일 이름
            long size = multipartFile.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(multipartFile.getContentType());
            objectMetaData.setContentLength(size);

            // S3에 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
            imagePathList.add(imagePath);
        }

//        return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
        return imagePathList;
    }
    public String upload2(byte[] decodedFile) throws Exception {
        //이름 랜덤으로 만들기위함.
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String name = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String image_path; //결과 url
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedFile);

//        BASE64DecodedMultipartFile base64DecodedMultipartFile = new BASE64DecodedMultipartFile(decodedFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("jpg");
        objectMetadata.setContentLength(decodedFile.length);
//        objectMetadata.setCacheControl("public, max-age=31536000");
//        amazonS3Client.putObject(S3Bucket,name,byteArrayInputStream,objectMetadata);
//        amazonS3Client.setObjectAcl(S3Bucket,name,CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, name, byteArrayInputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
//        image_path = amazonS3Client.getResourceUrl(S3Bucket, name).toString(); // 접근가능한 URL 가져오기


        image_path = amazonS3Client.getResourceUrl(S3Bucket, name);


        return image_path;
    }



}
