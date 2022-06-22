package by.itacademy.mail.view.api;

import by.itacademy.mail.model.FileData;

import java.util.UUID;
import java.util.concurrent.Future;

public interface StorageService {

    Future<FileData> download(UUID id);
}
