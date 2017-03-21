package com.exam.logic.actions.upload;

import com.exam.logic.Action;
import com.exam.logic.services.PhotoService;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.Validator;
import com.exam.models.Photo;
import com.exam.models.Profile;
import com.exam.models.User;
import lombok.extern.log4j.Log4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.exam.logic.Constants.*;

@Log4j
public class Avatar implements Action {
    private Random random = new Random();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new RuntimeException("Request is not multipart");
        }

        // Создаём класс фабрику
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Максимальный буфера данных в байтах,
        // при его привышении данные начнут записываться на диск во временную директорию
        // устанавливаем один мегабайт
        factory.setSizeThreshold(1024 * 1024);

        // устанавливаем временную директорию
        File tempDir = (File) request.getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        //Создаём сам загрузчик
        ServletFileUpload upload = new ServletFileUpload(factory);

        //максимальный размер данных который разрешено загружать в байтах
        //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт.
        upload.setSizeMax(1024 * 1024 * 10);

        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            if (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField() && !item.getName().isEmpty())
                    //в противном случае рассматриваем как файл
                    processUploadedFile(item, request);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ProfileService profileService = (ProfileService) request.getServletContext().getAttribute(PROFILE_SERVICE);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        Profile profile = profileService.getById(currentUser.getId());
        request.setAttribute(SUCCESS_MSG, "success.changes");
        request.setAttribute(PROFILE, profile);
        return "/WEB-INF/jsp/profile/edit.jsp";
    }

    /**
     * Сохраняет файл на сервере, в папке upload.
     *
     * @throws Exception, если проблема с FileItem
     */
    private void processUploadedFile(FileItem item, HttpServletRequest request) throws Exception {
        ServletContext context = request.getServletContext();
        File uploadetFile;
        String path = context.getRealPath("/files/");
        File directory = new File(path);
        if (!directory.exists()) directory.mkdir();
        //выбираем файлу имя пока не найдём свободное
        String link;
        do {
            link = Math.abs(random.nextInt()) + item.getName();
            String avaPath = path + link;
            uploadetFile = new File(avaPath);
        } while (uploadetFile.exists());

        //создаём файл
        uploadetFile.createNewFile();
        //записываем в него данные
        item.write(uploadetFile);
        //создаём уменьшённую коию
        createResizedImage(uploadetFile);
        //персиситим линку в базу
        addLinkToDB(link, request);
    }

    private Validator.ValidCode createResizedImage(File file) throws IOException {
        Validator.ValidCode code = Validator.ValidCode.INVALID_PIXEL_SIZE;
        try (ImageInputStream in = ImageIO.createImageInputStream(file)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(in);
                BufferedImage originaImage = reader.read(0);
                BufferedImage scaledImage = Scalr.resize(originaImage, 80);
                String path = file.getAbsolutePath();
                code = Validator.validateImage(reader);
                if (code == Validator.ValidCode.SUCCESS) {
                    log.debug("last index:" + path.lastIndexOf("\\"));
                    log.debug(path.substring(0, path.lastIndexOf("\\")) + "\\min_" + path.substring(path.lastIndexOf("\\")) + 1);
                    //если у файла нет расширение, то кидаем исключение
                    if (path.lastIndexOf("\\") > path.lastIndexOf("."))
                        throw new RuntimeException("Invalid file extension: " + path);
                    file = new File(path.substring(0, path.lastIndexOf(".")) + "_min" + path.substring(path.lastIndexOf(".")));
                    ImageIO.write(scaledImage, "jpg", file);
                }
            }
        }
        return code;
    }

    private void addLinkToDB(String path, HttpServletRequest request) {
        PhotoService service = (PhotoService) request.getServletContext().getAttribute(PHOTO_SERVICE);
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        Photo ava = Photo.builder()
                .avatar(true)
                .link(path)
                .sender(user.getId())
                .time(Instant.now())
                .build();
        service.savePhoto(ava);
    }
}
