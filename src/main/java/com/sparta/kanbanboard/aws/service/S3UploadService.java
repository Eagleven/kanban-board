package com.sparta.kanbanboard.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * putObject() 메소드가 파일을 저장해주는 메소드 getURl()을 통해 파일이 저장된 URL을 return 해주고, 이 URL로 이동 시 해당 파일이 오픈됨 (버킷
 * 정책 변경을 하지 않았으면 파일은 업로드 되지만 해당 URL로 이동 시 accessDenied 됨)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    /*
     * 로컬 파일 다운로드 할 때에는 UrlResource() 메소드에 "file:" + 로컬 파일 경로를 넣어주면 로컬 파일이 다운로드 되었음
     * S3에 올라간 파일은 위와 같이 amazonS3.getUrl(버킷이름, 파일이름)을 통해 파일 다운로드를 할 수 있음
     */

    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }

    public void deleteFile(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        amazonS3.deleteObject(bucket, key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            // 버킷 이름 이후의 경로를 반환
            return path.substring(path.indexOf("/", 1) + 1);
        } catch (MalformedURLException e) {
            log.error("Invalid URL: " + fileUrl, e);
            throw new IllegalArgumentException("Invalid file URL", e);
        }
    }
}
