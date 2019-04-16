/**
 * Created by KyleBai on 2017/3/28.
 * Update by Wang Hongxu  on 2019/4/6.
 */

package com.simple;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.rgw.RGWClient;

import java.util.List;
import java.io.File;

public class SimpleS3 {

    static final String ACCESS_KEY = "0555b35654ad1656d804";
    static final String SECRET_KEY = "h7GhxuBLTrlhVUyxSPUKUV8r/2EI4ngqJxD7iBdBYLhwluN30JaT3Q==";
    static final String HOSTNAME = "http://localhost:8000"; // THIS IS PRIVATE IP!!!
    static final String BUCKET_NAME = "example";
    static RGWClient client;

    public static void main(String [] argv) {
      try{
        client = new RGWClient(ACCESS_KEY, SECRET_KEY, HOSTNAME);
        
        System.out.println("----- create bucket [" + BUCKET_NAME + "] -----");
        client.createBucket(BUCKET_NAME);

        System.out.println("----- List all bucket -----");
        List<Bucket> buckets = client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.toString());
        }


        System.out.println("----- Update test.txt to bucket -----");
        client.putObject(new File("test.txt"), client.getBucket(BUCKET_NAME));
        System.out.println("----- Update test1.txt to bucket -----");
        client.putObject(new File("test1.txt"), client.getBucket(BUCKET_NAME));
        System.out.println("----- Update test2.txt to bucket -----");
        client.putObject(new File("test2.txt"), client.getBucket(BUCKET_NAME));


        System.out.println("\n----- Show bucket files-----");
        ObjectListing files_objects = client.listObjects(client.getBucket(BUCKET_NAME));
        List<S3ObjectSummary> objects = files_objects.getObjectSummaries();
        for (S3ObjectSummary object: objects) {
            System.out.println(object.getKey() + ", " + object.getSize() + " bytes");
        }

        System.out.println("\n----- Download files -----");
        client.downloadObject(BUCKET_NAME, "test.txt", new File("down-test.txt"));

        System.out.println("\n----- Delete files -----");
        client.deleteObject(BUCKET_NAME, "test.txt");
        client.deleteObject(BUCKET_NAME, "test1.txt");
        client.deleteObject(BUCKET_NAME, "test2.txt");

        System.out.println("\n----- Delete bucket -----");
        client.deleteObject(BUCKET_NAME, "test2.txt");
      }
      catch (Exception e){
        e.printStackTrace();
      }
    }

}
