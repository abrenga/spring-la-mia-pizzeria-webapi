package it.lamiapizzeria.storage;


import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties("storage")
public class StorageProperties {


        private String location = "upload_directory";

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }
