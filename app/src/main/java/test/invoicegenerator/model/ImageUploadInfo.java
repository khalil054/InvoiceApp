package test.invoicegenerator.model;


public class ImageUploadInfo {

    private String imageName;

    private String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url) {

        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}
