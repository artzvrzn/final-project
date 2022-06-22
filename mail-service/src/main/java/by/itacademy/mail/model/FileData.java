package by.itacademy.mail.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.util.Optional;

@Getter
@Setter
public class FileData {

    private String filename;
    private String extension;
    private byte[] content;
    private MediaType mediaType;
    private Long size;

    private FileData() {}

    public static class Builder {

        private static final String ERROR_MESSAGE = "Builder failed to build FileData: ";
        private final FileData fileData;

        public Builder() {
            this.fileData = new FileData();
        }

        public Builder withFilename(String filename) {
            fileData.filename = filename;
            return this;
        }

        public Builder withContent(byte[] bytes) {
            fileData.content = bytes;
            return this;
        }

        public FileData build() {
            if (fileData.filename == null || fileData.filename.isEmpty()) {
                throw new IllegalArgumentException(ERROR_MESSAGE + "filename is incorrect");
            }
            if (fileData.content == null) {
                throw new IllegalArgumentException(ERROR_MESSAGE + "content is null");
            }
            fileData.extension = FilenameUtils.getExtension(fileData.getFilename());
            fileData.size = (long) fileData.getContent().length;
            Optional<MediaType> optionalMediaType = MediaTypeFactory.getMediaType(fileData.filename);
            if (optionalMediaType.isEmpty()) {
                throw new IllegalArgumentException
                        (ERROR_MESSAGE + "failed to parse mediaType from " + fileData.filename);
            }
            fileData.mediaType = optionalMediaType.get();
            return fileData;
        }
    }
}
