package com.example.notice.files;

import com.example.notice.config.ConfigurationService;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.exception.PhysicalFileNotFoundException;
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
public class HardFileRepository implements PhysicalFileRepository{

    private final ConfigurationService configurationService;

    @Override
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException {
        String fullPath = configurationService.getFilePath() + "/" + getNewFilename(getExtension(originalFileName));

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

        //TODO 삭제 실패시도 FileSaveCheckedException 하도록
        // log를 찍어 사용자가 삭제를 실패해도 모르게 하지만 log를 마지막 부분에서 찍어야 한다.
        // interceptor? filter? 어디서 찍어야하는가? 혹은 rollback로그를 포함해야 하는가?
        // 실제 파일을 삭제하면 안되는가? rollback되면? 복구해야하는데?
        // 그냥 삭제되었다고 로그만 찍고 나중에 로그따라서 지우는게 좋아보이는데?
        // fullpath 로깅 or 간단하게 fk 지우고 file_id logging
        // throw 던지고 filter에서 logging? 그 이후 제어 불가능...
    }

    @Override
    public File getFile(String path) {
        File file = new File(path);
        if (isFileNotExist(file)) {
            throw new PhysicalFileNotFoundException(path);
        }

        return file;
    }

    private static boolean isFileNotExist(File file) {
        return !file.exists();
    }


    private static String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    private static String getNewFilename(String extension) {
        String uuid = UUID.randomUUID().toString();

        return System.nanoTime() + uuid + "." + extension;
    }
}
