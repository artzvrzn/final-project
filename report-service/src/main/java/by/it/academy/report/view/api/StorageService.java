package by.it.academy.report.view.api;

import by.it.academy.report.model.FileData;

public interface StorageService {

    void upload(FileData fileProperty);

    FileData download(String filename);
}
