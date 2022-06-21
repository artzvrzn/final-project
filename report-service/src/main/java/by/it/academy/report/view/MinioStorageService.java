package by.it.academy.report.view;

import by.it.academy.report.model.FileData;
import by.it.academy.report.view.api.StorageService;
import by.it.academy.report.view.handler.api.ReportHandler;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Log4j2
@Service
public class MinioStorageService implements StorageService {

    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public void upload(FileData file) {
        try (InputStream inputStream = new ByteArrayInputStream(file.getContent())) {
            log.info("Uploading {} to the storage", file.getFilename());
        minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(file.getFilename())
                    .contentType(file.getMediaType().toString())
                    .stream(inputStream, file.getSize(), -1)
                    .build());
        log.info("Report {} has been uploaded into the storage", file.getFilename());
        } catch (Exception e) {
            log.info("Error occurred during uploading the file {}", file.getFilename());
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileData download(String filename) {
        try {
            log.info("Downloading file {} from ste storage", filename);
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build());
            FileData file = new FileData.Builder()
                    .withFilename(filename)
                    .withContent(stream.readAllBytes())
                    .build();
            stream.close();
            log.info("File {} has been downloaded from the storage", filename);
            return file;
        } catch (Exception e) {
            log.info("Error occurred during downloading the file {}", filename, e.getCause());
            return null;
        }
    }
}
