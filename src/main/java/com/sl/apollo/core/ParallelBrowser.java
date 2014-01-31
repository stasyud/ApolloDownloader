package com.sl.apollo.core;

import com.sl.apollo.model.Resource;
import com.sl.apollo.parsers.ParserFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/**
 * Class that is used for downloading images and storing them to specified folder
 * User: SYudenkov
 * Date: 29.01.14
 */
public class ParallelBrowser {
    private static final Logger log = Logger.getLogger(ParallelBrowser.class);
    private int maxParallelTasks;
    private long downloadTimeout;

    private static String folder;

    private static ParallelBrowser instance = new ParallelBrowser();

    public static ParallelBrowser getInstance() {
        return instance;
    }

    private ParallelBrowser() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        folder = rb.getString("output.folder");
        maxParallelTasks = Integer.parseInt(rb.getString("max.parallel.downloads"));
        downloadTimeout = Long.parseLong(rb.getString("image.download.timeout"));
        prepareFolder(folder);
    }

    public void browse(List<Resource> imageSets, Resource album, Resource magazine) {
        if (imageSets != null && imageSets.size() > 0) {
            ExecutorService es = Executors.newFixedThreadPool(
                    imageSets.size() > maxParallelTasks ? maxParallelTasks : imageSets.size()
            );
            List<Future<Resource>> tasks = new ArrayList<>(imageSets.size());
            for (Resource resource : imageSets) {
                tasks.add(es.submit(new CallableBrowser(resource, magazine, album)));
            }
            for (Future<Resource> task : tasks) {
                try {
                    task.get(downloadTimeout, TimeUnit.SECONDS);
                } catch (ExecutionException e) {
                    log.error("Error browsing for mLink: mURL: " + magazine.getResourceURL() + "; album : " + album.getResourceURL(), e);
                } catch (InterruptedException ie) {
                    log.error("Browse interrupted for mLink: mURL: " + magazine.getResourceURL() + "; album : " + album.getResourceURL(), ie);
                } catch (TimeoutException e) {
                    log.warn("Browse timed-out for mLink:  mURL: " + magazine.getResourceURL() + "; album : " + album.getResourceURL());
                }
            }
            es.shutdown();
        }
    }

    public static class CallableBrowser implements Callable<Resource> {
        private Resource image;
        private Resource magazine;
        private Resource album;
        private String fileLocation;


        public CallableBrowser(Resource image, Resource magazine, Resource album) {
            this.image = image;
            this.magazine = magazine;
            this.album = album;
            fileLocation = getFileName();
            createFolder(fileLocation);
        }

        @Override
        public Resource call() throws Exception {
            List<Resource> resources = HtmlUtils.getInstance().parsePage(image.getResourceURL(), ParserFactory.IMAGE);
            if (resources != null && resources.size() > 0) {
                Resource res = resources.get(0);
                String fileName = fileLocation + res.getResourceName().replaceAll("(\"|'|,|;)","") + ".jpg";
                File file = new File(fileName);
                long start = System.currentTimeMillis();
                FileUtils.copyURLToFile(new URL(res.getImageURL()), file);
                start = System.currentTimeMillis() - start;
                if (log.isDebugEnabled())
                    log.debug("File: " + file.getName() + "; size: " + file.length() + "; it took: " + (start > 5000 ? start / 1000 + "s" : start + "ms"));
            }
            return null;
        }

        private String getFileName() {
            return folder + File.separator + album.getResourceName() + File.separator + magazine.getResourceName() + File.separator;
        }

    }

    /**
     * Method creates folder if it does not exist
     */
    public static File createFolder(String path) {
        File dir = new File(path);
        if (dir.exists()) return dir;
        dir.mkdirs();
        return dir;
    }

    /**
     * Initial method for preparing initial folder. Be aware it will delete all the content in the given folder if it exists
     */
    public static boolean prepareFolder(String folder) {
        File directory = new File(folder);
        if (directory != null && directory.exists())
            try {
                delete(directory);
            } catch (IOException e) {
                log.error("Error while deleting directory: " + folder, e);
                return false;
            }
        directory = new File(folder);
        directory.mkdirs();
        return true;
    }

    /**
     * Deletes given file
     */
    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            //check whether directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                log.info("Directory was deleted : " + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String[] files = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    log.info("Directory was deleted : " + file.getAbsolutePath());
                }
            }
        } else {
            //if given file is a plain file - delete it
            file.delete();
            log.info("File was deleted : " + file.getAbsolutePath());
        }
    }


}

