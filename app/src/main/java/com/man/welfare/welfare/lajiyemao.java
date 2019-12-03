package com.man.welfare.welfare;

import java.util.List;

public class lajiyemao {

    private int code;
    private Object error;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String ContentId;
        private String Name;
        private int ContentType;
        private String Content;
        private int OrderId;
        private String Publisher;
        private String PublishTime;
        private int Duration;

        public String getContentId() {
            return ContentId;
        }

        public void setContentId(String ContentId) {
            this.ContentId = ContentId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getContentType() {
            return ContentType;
        }

        public void setContentType(int ContentType) {
            this.ContentType = ContentType;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public int getOrderId() {
            return OrderId;
        }

        public void setOrderId(int OrderId) {
            this.OrderId = OrderId;
        }

        public String getPublisher() {
            return Publisher;
        }

        public void setPublisher(String Publisher) {
            this.Publisher = Publisher;
        }

        public String getPublishTime() {
            return PublishTime;
        }

        public void setPublishTime(String PublishTime) {
            this.PublishTime = PublishTime;
        }

        public int getDuration() {
            return Duration;
        }

        public void setDuration(int Duration) {
            this.Duration = Duration;
        }
    }
}
