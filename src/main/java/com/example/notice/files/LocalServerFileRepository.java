package com.example.notice.files;

import com.example.notice.config.ConfigurationService;
import com.example.notice.exception.FileSaveCheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalServerFileRepository implements PhysicalFileRepository{

    private final ConfigurationService configurationService;

    @Override
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException {
        String filePath = configurationService.getFilePath();
        String extension = getExtension(originalFileName);
        String newFilename = getNewFilename(extension);

        String fullPath = filePath + "/" + newFilename;
        try (OutputStream outputStream = new FileOutputStream(fullPath))
        {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.info("file save failed  fileName: {},  stackTrace{}", originalFileName, e);
            throw new FileSaveCheckedException(e.getMessage(), originalFileName);
        }

        return fullPath;
    }

    @Override
    public void delete(String fullPath) {
        File file = new File(fullPath);
        file.delete();
    }

    @Override
    public File getFile(String path) {
        return new File(path);
    }


    //    ==================================================================================================================
    private static String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    private static String getNewFilename(String extension) {
        String uuid = UUID.randomUUID().toString();

        return System.nanoTime() + uuid + "." + extension;
    }
}
