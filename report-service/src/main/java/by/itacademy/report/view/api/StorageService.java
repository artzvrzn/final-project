package by.itacademy.report.view.api;

import by.itacademy.report.model.FileData;

import java.util.concurrent.Future;

public interface StorageService {

    void upload(FileData fileProperty);

    Future<FileData> download(String filename);
}
