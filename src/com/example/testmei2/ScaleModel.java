package com.example.testmei2;

public class ScaleModel {

    private int initWidth;
    private int initHeight;
    private int rationWidth;
    private int ratioHeight;
    private int minWidth;
    private int minHeight;

    public ScaleModel(int initWidth, int initHeight, int rationWidth,
            int ratioHeight, int minWidth, int minHeight) {
        super();
        this.initWidth = initWidth;
        this.initHeight = initHeight;
        this.rationWidth = rationWidth;
        this.ratioHeight = ratioHeight;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    /**
     * @return the initWidth
     */
    public int getInitWidth() {
        return initWidth;
    }

    /**
     * @param initWidth the initWidth to set
     */
    public void setInitWidth(int initWidth) {
        this.initWidth = initWidth;
    }

    /**
     * @return the initHeight
     */
    public int getInitHeight() {
        return initHeight;
    }

    /**
     * @param initHeight the initHeight to set
     */
    public void setInitHeight(int initHeight) {
        this.initHeight = initHeight;
    }

    /**
     * @return the rationWidth
     */
    public int getRationWidth() {
        return rationWidth;
    }

    /**
     * @param rationWidth the rationWidth to set
     */
    public void setRationWidth(int rationWidth) {
        this.rationWidth = rationWidth;
    }

    /**
     * @return the ratioHeight
     */
    public int getRatioHeight() {
        return ratioHeight;
    }

    /**
     * @param ratioHeight the ratioHeight to set
     */
    public void setRatioHeight(int ratioHeight) {
        this.ratioHeight = ratioHeight;
    }

    /**
     * @return the minWidth
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     * @param minWidth the minWidth to set
     */
    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * @return the minHeight
     */
    public int getMinHeight() {
        return minHeight;
    }

    /**
     * @param minHeight the minHeight to set
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

}
