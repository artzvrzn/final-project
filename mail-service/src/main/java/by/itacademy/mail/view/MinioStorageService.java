package by.itacademy.mail.view;

import by.itacademy.mail.model.FileData;
import by.itacademy.mail.view.api.StorageService;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;

@Log4j2
@Service
public class MinioStorageService implements StorageService {

    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public Future<FileData> download(UUID id) {
        String filename = getFilenameById(id);
        try {
            log.info("Downloading file {} from the storage...", filename);
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
            return new AsyncResult<>(file);
        } catch (Exception e) {
            log.error("Error occurred during downloading the file {}", filename, e.getCause());
            return null;
        }
    }

    private String getFilenameById(UUID id) {
        log.info("Looking for file with id {}...", id);
        String stringId = id.toString();
        List<String> files = getListObjects();
        Optional<String> optional =  files.stream()
                .filter(s -> FileNameUtils.getBaseName(s).equals(stringId))
                .findFirst();
        if (optional.isEmpty()) {
            log.info("File with id {} doesn't exist", stringId);
            throw new IllegalArgumentException(String.format("File %s doesn't exist)", stringId));
        }
        return optional.get();
    }

    private List<String> getListObjects() {
        log.info("Reading files in storage....");
        List<String> files = new ArrayList<>();
        try {
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build());
            for (Result<Item> item : result) {
                files.add(item.get().objectName());
            }
            return files;
        } catch (Exception e) {
            log.error("Error occurred while reading files in storage: ", e);
        }
        return files;
    }
}
