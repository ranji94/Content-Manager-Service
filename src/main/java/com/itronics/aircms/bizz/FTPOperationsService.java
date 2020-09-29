package com.itronics.aircms.bizz;

import com.google.gson.Gson;
import com.itronics.aircms.common.FtpClient;
import com.itronics.aircms.domain.News;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class FTPOperationsService {
    public static final Logger logger = Logger.getLogger(FTPOperationsService.class.getName());
    private static final String NEWS_FILENAME = "news.json";
    private final Gson gson = new Gson();

    public List<News> downloadNewsFeed() throws IOException {
        FtpClient ftpClient = FtpClient.getInstance();
        String downloadStatus = ftpClient.downloadFile("/" + NEWS_FILENAME, NEWS_FILENAME);
        logger.info(String.format("[DownloadNewsFeed] Download news status: %s", downloadStatus));

        return new ArrayList<>(Arrays.asList(gson.fromJson(new FileReader(NEWS_FILENAME), News[].class)));
    }

    public List<News> uploadNewsFeed(List<News> newsList) throws IOException {
        FtpClient ftpClient = FtpClient.getInstance();
        String newsFeedString = gson.toJson(newsList);

        BufferedWriter writer = new BufferedWriter(new FileWriter(NEWS_FILENAME));
        writer.write(newsFeedString);
        writer.close();

        ftpClient.uploadFile(NEWS_FILENAME, "/" + NEWS_FILENAME);

        return newsList;
    }
}
