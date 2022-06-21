package by.it.academy.report.view.api;

import by.it.academy.report.model.FileData;

import java.util.concurrent.Future;

public interface StorageService {

    void upload(FileData fileProperty);

    Future<FileData> download(String filename);
}
