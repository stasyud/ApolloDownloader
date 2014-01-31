package com.sl.apollo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO class for keeping and passing information within the system
 * User: SYudenkov
 * Date: 29.01.14
 */
public class Resource {
    private String resourceURL;
    private String resourceName;
    private String description;
    private List<Resource> resourceContentURLs;
    private boolean image;
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Resource> getResourceContentURLs() {
        return resourceContentURLs;
    }

    public void setResourceContentURLs(List<Resource> resourceContentURLs) {
        this.resourceContentURLs = resourceContentURLs;
    }

    public void addResourceContentURLs(Resource resource) {
        if (resourceContentURLs == null) resourceContentURLs = new ArrayList<>();
        resourceContentURLs.add(resource);
    }
}
